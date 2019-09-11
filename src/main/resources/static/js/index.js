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
       '<div class="message-bar">' +
       '<span class="username">' + message.username + '</span>' +
       '<span class="datetime">' +  message.datetime + '</span>';
       if (message.deleted == null) {
         if (current_username_ == message.username) {
           messageHtml += '<button class="msg-menu-btn"><i class="fas fa-ellipsis-h"></i></button>';
         } else {
           messageHtml += '<button class="reply-btn" title="ответить"><i class="fas fa-level-up-alt"></i></button>';
         }
       }
       messageHtml += '</div>';
       message.messagesToReply.forEach(function(msgToRply, index, array) {
         messageHtml += '<div class="message-to-reply-wrapper">' +
                          '<div class="message-to-reply">' +
                            '<div class="icon"><i class="fas fa-level-down-alt"></i></div>' +
                            '<div class="name">' +
                              '<span>' + msgToRply.username + '</span>' +
                            '</div>' +
                            '<div class="datetime">' +
                              '<span>' + msgToRply.datetime+ '</span>' +
                            '</div>';
         if (msgToRply.deleted != null)  {
           messageHtml += '<span class="text message-deleted">сообщение удалено</span>';
         } else {
           messageHtml += '<span class="text">' + msgToRply.text + '</span>';
         }
         messageHtml +=
                          '</div>' +
                          '</div>';

        });
       if (message.deleted != null)  {
         messageHtml += '<span class="message message-deleted">сообщение удалено</span>';
       } else {
         messageHtml += '<span class="message';
         if (message.modified != null) {
           messageHtml += ' modified">' + message.text;
         } else {
           messageHtml += '">' + message.text;
         }
       }
     messageHtml +=
     '</span>';
     if (message.modified != null) {
       messageHtml += '<i class="message-modified-icon fas fa-pencil-alt"></i>';
     }
     messageHtml += '</div>';

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
  lastDeletedMessageId = messages[Object.keys(messages).length-1].id;
  $.each(messages, function(k, message) {
    $(".message-wrapper[data-message-id='" + message.id + "']").each(function(j, frontMessage) {
      $(frontMessage).find('.message').text('сообщение удалено');
      $(frontMessage).find('.message').addClass('message-deleted');
      $(frontMessage).find(".msg-menu-btn").remove();
      $(frontMessage).find('.message-modified-icon').remove();
    });
  });
}

function processModifiedMessages(messages) {
  lastModifiedMessageDatetime = messages[Object.keys(messages).length-1].modified;
  $.each(messages, function(k, message) {
    $(".message-wrapper[data-message-id='" + message.id + "']").each(function(j, frontMessage) {
      if (message.deleted == null) {
        $(frontMessage).find('.message').text(message.text);
        $(frontMessage).find('.message').removeClass('not-modified');
        $(frontMessage).find('.message').addClass('modified');
        if ($(frontMessage).find('.message-modified-icon').length == 0) {
          $(frontMessage).append('<i class="message-modified-icon fas fa-pencil-alt"></i>');
        }
      }
    });
  });
}

function processLongPollResponse(response) {
  if (response.type == 'NEW_MESSAGES') {
    appendMessages(response.messages);
  } else if (response.type == 'NEW_DELETED_MESSAGES') {
    processDeletedMessages(response.messages);
  } else if (response.type == 'NEW_MODIFIED_MESSAGES') {
    processModifiedMessages(response.messages);
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
  $('.reply-btn').click( function() { addMessageToReply( $(this).parent().parent() ); } );
  $('.msg-menu-btn').click( function() {
    messageMenuCurrentMessage = $(this).parent().parent();
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
  requestData['firstMessageId'] = parseInt($(".message-wrapper").first().attr("data-message-id"), 10);
  if ($(".message-wrapper").length == 0) {
    requestData['lastMessageId'] = -1;
  } else {
    requestData['lastMessageId'] = parseInt($(".message-wrapper").last().attr("data-message-id"), 10)
  }
  requestData['lastDeletedMessageId'] = lastDeletedMessageId;
  requestData['lastModifiedMessageDatetime'] = lastModifiedMessageDatetime;
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
  $('.message-menu-modify-btn').click(function() {
    $('#message-menu-modal').modal('hide');
    console.log('messageMenuCurrentMessage : ' + messageMenuCurrentMessage.find('.message').text());
    $('#modify-message-textarea').remove();
    $('#modify-modal-content').prepend('<textarea class = "modify-message-textarea" id="modify-message-textarea">' +
                                        messageMenuCurrentMessage.find('.message').text() + '</textarea>');
    $('#modify-modal').modal('show');
  });
}

$(document).ready(function() {
   initMessageMenuModal();

   initCustomScrollbar('#messages-container');
   $('#messages-container').show();
   initCustomScrollbar('#message-textarea');
   $('#message-textarea').show();
   initCustomScrollbar('#message-textarea');
   $('#message-textarea').show();
   scrollToTheEnd(0);

   bindMessagesActions();

   $('#send-message-btn').click(function() {
     var message = {}
     var messageText = $('#message-textarea').val();
     if (messageText.trim() == '') {
       return;
     }
     message['text'] = messageText;
     message.messagesToReply = [];
     $('.messages-to-reply').children().each(function(index) {
         messageToReply = {}
         messageToReply['id'] = $(this).attr('data-message-id');
         message.messagesToReply.push(messageToReply);
     })
     $('#message-textarea').val('');
     sendMessage(message, function() {  },
                          function() {  },
                           hostURL);
     $('.messages-to-reply').empty();
     hideMessagesToReplyBlock();
   });

   $('#modify-message-send-btn').click(function() {
     var message = {}
     var messageText = $('#modify-message-textarea').val();
     if (messageText.trim() == '') {
       return;
     }
     message['text'] = messageText;
     message['id'] = parseInt(messageMenuCurrentMessage.attr('data-message-id'), 10);
     message['modified'] = messageMenuCurrentMessage.attr('data-message-modified');
     $('#modify-message-textarea').val('');
     sendModifiedMessage(message, function() { },
                          function() { },
                          function() { },
                           hostURL);
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