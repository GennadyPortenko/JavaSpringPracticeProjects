function longPoll(prepareRequestData, processMessages, hostURL) {
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: hostURL + "/messenger/poll",
    data: JSON.stringify(prepareRequestData()),
    dataType: "json",
    mimeType: 'application/json',
    cache: false,
    // timeout: 600000,
    complete: function(request, textStatus){
      switch(textStatus) {
        case "success" :
          var messages = JSON.parse(request.responseText);
          processMessages(messages);
          longPoll(prepareRequestData, processMessages, hostURL);
          break;
        case "nocontent" :  //  long polling timeout (no new messages available)
          // console.log("no content");
          longPoll(prepareRequestData, processMessages, hostURL);
          break;
        default :  //  error or server unavailable
          setTimeout(longPoll(prepareRequestData, processMessages, hostURL), 2000);
          break;
      }
    }
  });
}
