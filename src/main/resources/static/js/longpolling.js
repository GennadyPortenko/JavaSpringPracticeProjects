function longPoll(requestData) {
      console.log("performing ajax call ...");
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
             console.log("success");
             $.each(JSON.parse(request.responseText), function(k, v) {
               console.log(v);
             });
             longPoll(requestData);
             break;
           case "nocontent" :  //  long polling timeout (no new messages available)
             console.log("no content");
             longPoll(requestData);
             break;
           default :  //  error or server unavailable
             setTimeout(longPoll, 2000);
             break;
         }
       },
       error: function(){
        console.log('Error while performing request..');
       }
      });
     }
