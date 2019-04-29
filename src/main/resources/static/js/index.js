$(document).ready(function() {
  var requestData = {};
  requestData["lastMessageId"] = 0;
  longPoll(requestData);
});