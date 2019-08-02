function deleteMessage(/* requestData, */ messageId, onSuccess, onError, onNocontent, hostURL) {
  console.log("sending message delete request: " + messageId);
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: hostURL + "/messenger/message/delete/" + messageId,
    data: {},
    dataType: "json",
    cache: false,
    /*
    success: function(data, textStatus, response) {
      onSuccess();
    },
    nocontent: function() {
      onNocontent();
    },
    error: function(response, textStatus, errorThrown) {
      onError();
    },
    */
    complete: function(response, textStatus){
      switch(textStatus) {
        case "success" :
          onSuccess()
          break;
        case "nocontent" : // message is already set as deleted
          onNocontent();
          break;
        default :
          onError();
          break;
      }
    }
  });
}
