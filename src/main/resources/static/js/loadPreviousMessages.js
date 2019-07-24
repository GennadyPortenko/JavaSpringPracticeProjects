function loadPreviousMessages(requestData, onSuccess, onError, hostURL) {
  console.log('sending a request for previous messages');
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: hostURL + "/messenger/load_more",
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
