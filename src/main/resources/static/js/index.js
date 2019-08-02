var hostURL = "http://localhost:8080"
// var hostURL = "https://spring-mvc-chat.herokuapp.com";

var messageMenuCurrentMessage = null;

function showInfoMessage(infoMessage) {
  $('#info-modal-message').text(infoMessage);
  $('#info-modal').modal('show');
}

function scrollToTheEnd(duration) {
  $("#messages-container").animate({
    scrollTop:$("#messages-container")[0].scrollHeight - $(".message").last().height()
  }, duration)
}

function scrollToTheMiddle(duration) {
  $("#messages-container").animate({
    scrollTop:$("#load-previous-messages-btn").offset().top - 100
  }, duration)
}

function prepareMessageHtml(message) {
  var messageHtml =
    '<div data-message-id=' + message.id + ' class="message-wrapper message-wrapper-left ';
    if (current_username_ == message.username) {
      messageHtml += 'message-wrapper-me">';
    } else {
      messageHtml += 'message-wrapper-notme">';
    }
      messageHtml +=
       '<span class="username">' + message.username + '</span>' +
       '<span class="datetime">' +  message.datetime + '</span>';
       if (!message.deleted) {
         if (current_username_ == message.username) {
           messageHtml += '<button class="msg-menu-btn"><i class="fas fa-ellipsis-h"></i></button>';
         } else {
           messageHtml += '<button class="reply-btn" title="ответить"><i class="fas fa-reply"></i></button>';
         }
       }
       message.messagesToReply.forEach(function(msgToRply, index, array) {
         messageHtml += '<div class="message-to-reply-wrapper">' +
                          '<div class="message-to-reply">' +
                            '<div class="info">' +
                              '<span>' + msgToRply.username + '</span> писал :' +
                            '</div>';
         if (msgToRply.deleted)  {
           messageHtml += '<span class="text message-deleted">сообщение удалено</span>';
         } else {
           messageHtml += '<span class="text">' + msgToRply.text + '</span>';
         }
         messageHtml +=
                          '</div>' +
                          '</div>';

        });
       if (message.deleted)  {
         messageHtml += '<span class="message message-deleted">сообщение удалено</span>';
       } else {
         messageHtml += '<span class="message">' + message.text + '</span>';
       }
     messageHtml +=
     '</div>';

     return messageHtml;
}

function appendMessages(messages) {
  if (messages.length == 0) {
    return;
  }
  $.each(messages, function(k, message) {
    $("#messages-container-content").append(prepareMessageHtml(message));
  });
  $('#load-previous-messages-btn').removeClass('hidden');
  $('#no-messages-yet').addClass('hidden');
  scrollToTheEnd(500);
  bindMessagesActions();
}

function processDeletedMessages(messages) {
  if (messages.length == 0) {
    return;
  }
  $.each(messages, function(k, message) {
    $(".message-wrapper[data-message-id='" + message.id + "']").each(function(j, frontMessage) {
      $(frontMessage).find('.message').text('сообщение удалено');
      $(frontMessage).find('.message').addClass('message-deleted');
    });
  });
}

function processLongPollResponse(response) {
  if (response.type == 'NEW_MESSAGES') {
    appendMessages(response.messages);
  } else if (response.type == 'NEW_DELETED_MESSAGES') {
    processDeletedMessages(response.messages);
  }
}

function prependMessages(messages) {
  if (messages.length == 0) {
    $("#load-previous-messages-btn").remove();
    $("#messages-container-content").prepend(
      '<div class="no-more-messages-label">Это все сообщения</div>'
    );
    return;
  }
  $.each(messages, function(k, message) {
    $("#messages-container-content").prepend(prepareMessageHtml(message));
  });
  scrollToTheMiddle(0);
  $("#load-previous-messages-btn").remove();
  $("#messages-container-content").prepend(
    '<button class="load-previous-messages-btn" id="load-previous-messages-btn">загрузить еще</button>'
  );
  bindMessagesActions();
  bindLoadPreviousMessagesBtn();
}

function hideMessagesToReplyBlock() {
  $('.message-textarea').removeClass('shifted');
  $('.messages-to-reply').addClass('hidden');
}
function showMessagesToReplyBlock() {
  $('.messages-to-reply').removeClass('hidden');
  $('.message-textarea').addClass('shifted');
}

function bindLoadPreviousMessagesBtn() {
  $('#load-previous-messages-btn').click(function() {
     var data = {};
     data['id'] = parseInt($(".message-wrapper").first().attr("data-message-id"), 10);
     loadPreviousMessages(
         data,
         function(data) {
           prependMessages(data);
         },
         function() {
           console.log('error while sending a request for messages request');
         },
         hostURL
     );
   });
}

function bindMessagesActions() {
  $('.reply-btn').click( function() { addMessageToReply( $(this).parent() ); } );
  $('.msg-menu-btn').click( function() {
    messageMenuCurrentMessage = $(this).parent();
    $('.message-menu-msg-text').remove();
    $('#message-menu-content').prepend('<div class="message-menu-msg-text">' + messageMenuCurrentMessage.find('.message').text() + '</div>');
    if (messageMenuCurrentMessage.children('.message-deleted').length != 0) {
      $('.message-menu-delete-btn').hide()
      $('.message-menu-modify-btn').hide()
    } else {
      $('.message-menu-delete-btn').show()
      $('.message-menu-modify-btn').show()
    }
    $('#message-menu-modal').modal('show');
  });
  $('.message-to-reply > .close').click(function() {
    $(this).parent().remove();
    if ( $('.messages-to-reply').children().length == 0 ) {
      hideMessagesToReplyBlock();
    }
  });
}

function messageToReplyAlreadyAdded(message) {
  let n = $('#messages-to-reply').find('.message-to-reply[data-message-id = ' + message.attr('data-message-id') + ']').length;
  if (n != 0) {
    return true;
  } else {
    return false;
  }
}

function addMessageToReply(message) {
  if (messageToReplyAlreadyAdded(message)) {
    return;
  }
  var messageToReply = '<div class="message-to-reply" data-message-id="'+ message.attr('data-message-id') +'">' +
                           '<button class="close">x</button>' +
                           '<span class="text">' + message.find('.message').text() + '</span>'
  $('#messages-to-reply').append(messageToReply)
  showMessagesToReplyBlock();
  bindMessagesActions();
}

function activateMessageMenu(message) {
  $('#message-menu-content').find('.message-menu-msg-text').remove();
  $('#message-menu-content').prepend('<div class="message-menu-msg-text">' + messageMenuCurrentMessage.find('.message').text() + '</div>')
}

function prepareLongPollRequest() {
  var requestData = {};
  requestData['lastMessageId'] = parseInt($(".message-wrapper").last().attr("data-message-id"), 10);
  return requestData;
}

function initMessageMenuModal() {
  $('.message-menu-reply-btn').click(function() {
    addMessageToReply( messageMenuCurrentMessage );
    $('#message-menu-modal').modal('hide');
  });
  $('.message-menu-delete-btn').click(function() {
    $('#message-menu-modal').modal('hide');
    deleteMessage(messageMenuCurrentMessage.attr("data-message-id"),
            function() {
              console.log('message deleted')
              messageMenuCurrentMessage.find(".message").text('сообщение удалено');
              messageMenuCurrentMessage.find(".message").addClass('message-deleted')
              messageMenuCurrentMessage.find(".msg-menu-btn").remove();
            },
            function() {
              console.log('error while message deleting!');
              showInfoMessage('Ошибка. Сообщение не удалено.');
            },
            function() {
              console.log('message is already deleted!');
              showInfoMessage('Сообщение уже удалено.');
            },
            hostURL
    );
  });
}

$(document).ready(function() {
   initMessageMenuModal();

   initCustomScrollbar('#messages-container');
   $('#messages-container').show();
   initCustomScrollbar('#message-textarea');
   $('#message-textarea').show();
   scrollToTheEnd(0);

   bindMessagesActions();

   $('#send-message-btn').click(function() {
     var message = {}
     message['text'] = $('#message-textarea').val();
     message.messagesToReply = [];
     $('.messages-to-reply').children().each(function(index) {
         messageToReply = {}
         messageToReply['id'] = $(this).attr('data-message-id');
         message.messagesToReply.push(messageToReply);
     })
     if (message['text'] != '') {
       $('#message-textarea').val('');
       sendMessage(message, function() {  },
                            function() {  },
                             hostURL);
     }
     $('.messages-to-reply').empty();
     hideMessagesToReplyBlock();
   });

   $('#message-textarea').keyup(function (e) {
     if (e.keyCode == 13 && !e.shiftKey) {
       $('#send-message-btn').click();
       return false;
     }
   });

   bindLoadPreviousMessagesBtn();

  longPoll(prepareLongPollRequest, processLongPollResponse, hostURL);
});