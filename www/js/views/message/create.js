(function() {

  footprint.views.MessageCreateView = Parse.View.extend({

    id: 'makeMessage',

    events: {
      'click #upload-picture': 'capturePhoto',
      'click #upload_message': 'submit'
    },

    initialize: function() {
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('message_create'));
    },

    render: function () {
      var _this = this;

      $(this.el).append(this.template({}));
      $(this.el).addClass('scroll');
      var timeout = setTimeout(function() {
        // we need to wait a split-second before we attach and activate the scrollbars
        _this.iscroll = new iScroll(_this.el, {hScrollbar:false,vScrollbar:false});
        clearTimeout(timeout);
      }, 200);
      return this;
    },

    submit: function() {
      var message_text = this.$el.find("input[name='message_text']").val();
      var message_recipients = this.$el.find("input[name='recipients']").val();

      var Message = Parse.Object.extend("Message");
      var message = new Message();

      var serverUrl = 'https://api.parse.com/1/files/temp.jpg';
      console.log('serverurl '+serverUrl);

      if (this.imageData !== undefined) {
        $.ajax({
          type: "POST",
          beforeSend: function(request) {
            request.setRequestHeader("X-Parse-Application-Id", 'GejzV5ros8VQQleYBVpXRWhlSw4nZMmRbevIpI1g');
            request.setRequestHeader("X-Parse-REST-API-Key", 'J0GNoC5iJT3dF0FRdYmq8oLm44Xi5lIHmaFsxUrT');
            request.setRequestHeader("Content-Type", 'image/jpeg');
          },
          url: serverUrl,
          data: this.imageData,
          processData: false,
          contentType: false,
          success: function(data) {
            //Change variable to reflect your class to upload to
            var classUrl = "https://api.parse.com/1/classes/Message"

            if(data) {
              var fileName = "" + data.name;
              console.log(data).url;
              $.ajax({
                type: "POST",
                beforeSend: function(request) {
                  request.setRequestHeader("X-Parse-Application-Id", 'GejzV5ros8VQQleYBVpXRWhlSw4nZMmRbevIpI1g');
                  request.setRequestHeader("X-Parse-REST-API-Key", 'J0GNoC5iJT3dF0FRdYmq8oLm44Xi5lIHmaFsxUrT');
                  request.setRequestHeader("Content-Type", 'application/json');}
                ,
                url: classUrl,
                data: '{\"name\" : \"An Image\", \"image\" : {\"name\" : '+"\""+fileName+"\""+', \"__type\" : \"File\"}}',
                processData: false,

                success: function(data) {
                  console.log("Image successfully uploaded.");
                  footprint.iscroll.refresh();
                  footprint.iscroll.scrollTo(0,0,0);
                },
                error: function(error) {
                  console.log("Error: " + error.message);
                }
              });
            }
          },
          error: function(data) {
            var obj = jQuery.parseJSON(data);
            alert(obj.error);
          }
        });
      } else {
        message.set('message', message_text);
        message.set('recipients', message_recipients);
        message.save(null,
                     { success: function() {
                       console.log('holy batman! message saved successfully');
                     }, error: function() {
                       console.log('message failed to save to parse');
                     }
                     });
      }

      return false;
    },

    capturePhoto: function() {
      var self = this;

      if(!navigator.camera){
        alert("You don't have camera support!");
        return false;
      }

      navigator.camera.getPicture(onSuccess, onFail, { quality: 10,
                                                       destinationType: Camera.DestinationType.DATA_URL });

      function onSuccess(imageData) {
        var image = document.getElementById('my_picture');
        image.src = "data:image/jpeg;base64," + imageData;
        self.imageData = imageData;
      }

      function onFail(message) {
        navigator.notification.alert('Failed because: ' + message);
      }

      return false;
    },
  });
}).call(this)
