function sendMessage(requestData, onSuccess, onError, hostURL) {
  console.log("sending message : '" + requestData["text"] + "'");
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: hostURL + "/messenger/new_message",
    data: JSON.stringify(requestData),
    dataType: "json",
    cache: false,
    success: function(data, textStatus, response) {
      onSuccess(data);
    },
    nocontent: function() {
      console.log("sendMessage() : no content!")
    },
    error: function(response, textStatus, errorThrown) {
      onError();
    }
  });
}
