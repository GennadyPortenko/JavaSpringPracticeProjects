function sendMessage(requestData, onSuccess, onError) {
  console.log("sending message : " + requestData["text"]);
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "http://localhost:8080/messenger/new_message",
    data: JSON.stringify(requestData),
    dataType: "json",
    cache: false,
    success: function(data, textStatus, response) {
      onSuccess(data);
    },
    error: function(response, textStatus, errorThrown) {
      onError();
    }
  });
}
