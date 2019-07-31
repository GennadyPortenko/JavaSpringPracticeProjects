var hostURL = "http://localhost:8080"
// var hostURL = "https://spring-mvc-chat.herokuapp.com";

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
       '<span class="datetime">' +  message.datetime + '</span>' +
       '<button class="reply-btn">ответить</button>';
        message.messagesToReply.forEach(function(msgToRply, index, array) {
         messageHtml += '<div class="message-to-reply-wrapper">' +
                          '<div class="message-to-reply">' +
                            '<div class="info">' +
                              '<span>' + msgToRply.username + '</span> писал :' +
                            '</div>' +
                            '<div class="text">' + msgToRply.text + '</div>' +
                          '</div>' +
                          '</div>';

        });
       messageHtml +=
       '<span class=message>' + message.text + '</span>' +
     '</div>';

     return messageHtml;
}

function appendMessages(messages) {
  $.each(messages, function(k, message) {
    $("#messages-container-content").append(prepareMessageHtml(message));
  });
  scrollToTheEnd(500);
  bindMessagesToReply();
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
  bindMessagesToReply();
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

function bindMessagesToReply() {
  $('.reply-btn').click( function() { addMessageToReply( $(this).parent() ); } );
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
  bindMessagesToReply();
}

function prepareLongPollRequest() {
  var requestData = {};
  requestData['lastMessageId'] = parseInt($(".message-wrapper").last().attr("data-message-id"), 10);
  return requestData;
}

$(document).ready(function() {

   initCustomScrollbar('#messages-container');
   $('#messages-container').show();
   initCustomScrollbar('#message-textarea');
   $('#message-textarea').show();
   scrollToTheEnd(0);

   bindMessagesToReply();

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

  longPoll(prepareLongPollRequest, appendMessages, hostURL);
});