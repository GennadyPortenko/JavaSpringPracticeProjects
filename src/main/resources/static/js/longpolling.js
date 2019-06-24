function longPoll(requestData, processMessages, hostURL) {
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: hostURL + "/messenger/poll",
    data: JSON.stringify(requestData),
    dataType: "json",
    cache: false,
    // timeout: 600000,
    complete: function(request, textStatus){
      switch(textStatus) {
        case "success" :
          var messages = JSON.parse(request.responseText);
          processMessages(messages);
          longPoll(requestData, processMessages, hostURL);
          break;
        case "nocontent" :  //  long polling timeout (no new messages available)
          // console.log("no content");
          longPoll(requestData, processMessages, hostURL);
          break;
        default :  //  error or server unavailable
          setTimeout(longPoll(requestData, processMessages, hostURL), 2000);
          break;
      }
    }
  });
}
