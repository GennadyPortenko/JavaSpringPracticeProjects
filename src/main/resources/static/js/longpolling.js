function longPoll(requestData, processMessages) {
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "http://localhost:8080/messenger/poll",
    data: JSON.stringify(requestData),
    dataType: "json",
    cache: false,
    // timeout: 600000,
    complete: function(request, textStatus){
      switch(textStatus) {
        case "success" :
          var messages = JSON.parse(request.responseText);
          processMessages(messages);
          longPoll(requestData, processMessages);
          break;
        case "nocontent" :  //  long polling timeout (no new messages available)
          // console.log("no content");
          longPoll(requestData, processMessages);
          break;
        default :  //  error or server unavailable
          setTimeout(longPoll(requestData, processMessages), 2000);
          break;
      }
    }
  });
}
