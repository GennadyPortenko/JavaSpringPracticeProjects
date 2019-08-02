function longPoll(prepareRequestData, processLongPollResponse, hostURL) {
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
          processLongPollResponse(messages);
          longPoll(prepareRequestData, processLongPollResponse, hostURL);
          break;
        case "nocontent" :  //  long polling timeout (no new messages available)
          // console.log("no content");
          longPoll(prepareRequestData, processLongPollResponse, hostURL);
          break;
        default :  //  error or server unavailable
          setTimeout(longPoll(prepareRequestData, processLongPollResponse, hostURL), 2000);
          break;
      }
    }
  });
}
