function sendModifiedMessage(requestData, onSuccess, onError, onNocontent, hostURL) {
  console.log("sending message : '" + requestData["text"] + "'");
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: hostURL + "/messenger/message/modify",
    data: JSON.stringify(requestData),
    dataType: "json",
    cache: false,
    complete: function(response, textStatus){
      switch(textStatus) {
        case "success" :
          onSuccess()
          break;
        case "nocontent" :
          onNocontent();
          break;
        default :
          onError();
          break;
      }
    }
  });
}
