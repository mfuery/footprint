(function() {

  /**
   *
   */
  footprint.views.MessageCreateView = Parse.View.extend({

    id: 'makeMessage',

    events: {
      'click #upload-picture': 'capturePhoto',
      'click #render_page_two': 'renderPageTwo',
      'click #upload_message': 'submit'
    },

    initialize: function() {
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('messageCreate'));
      this.imageData = '';
    },

    render: function () {
      var _this = this;
      console.log('render');
      $(this.el).append(this.template({ 'content': 'this content is json dynamic!', 'id': '1', 'notes': 'notes' }));
      $(this.el).addClass('scroll');
      var timeout = setTimeout(function() {
        // we need to wait a split-second before we attach and activate the scrollbars
        _this.iscroll = new iScroll(_this.el, {hScrollbar:false,vScrollbar:false});
        clearTimeout(timeout);
      }, 200);
      return this;
    },

    submit: function() {
      var message_text = this.$el.find("input[name='text']").val();
      var message_recipients = this.$el.find("input[name='recipients']").val();

      var Message = Parse.Object.extend("Message");
      var message = new Message();
      //      message.set('message', message_text);
      //      message.set('recipients', message_recipients);

      //console.log('imagedata: '+this.imageData);
      var serverUrl = 'https://api.parse.com/1/files/temp.jpg';
      console.log('serverurl '+serverUrl);

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

          // console.log(data.name);
          // console.log(data);
          // navigator.notification.alert('holy fucking shit batman', null);

          // message.save({ message: message_text,
          //                recipients: message_recipients,
          //                image: {
          //                  name: data.name,
          //                  __type: "File"
          //                }
          //              },
          //              { success: function() {
          //                navigator.notification.alert('successful save', null);
          //              }, error: function() {
          //                navigator.notification.alert('is this lvoe?', null);
          //              }});
        },
        error: function(data) {
          var obj = jQuery.parseJSON(data);
          alert(obj.error);
        }
      });

      message.set('message', message_text);
      message.set('recipients', message_recipients);
      message.save(null,
                   { success: function() {
                     navigator.notification.alert('successful save', null);
                   }, error: function() {
                     navigator.notification.alert('is this lvoe?', null);
                   }});
      return false;
    },

    renderPageTwo: function () {
      var _this = this;
      var second_template = Handlebars.compile(footprint.utils.templateLoader.get('messageEdit'));
      $(this.el).html(second_template({ title: 'hello world', id: 'goodbye world' }));
      var timeout = setTimeout(function() {
        // we need to wait a split-second before we attach and activate the scrollbars
        _this.iscroll.refresh();
        clearTimeout(timeout);
      }, 200);

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

      // function onSuccess(imageURI) {
      //   var image = document.getElementById('my_picture');
      //   image.src = imageURI;
      //   self.imageURI = imageURI;
      //   navigator.notification.alert(imageURI, null);
      // }
      function onSuccess(imageData) {
        var image = document.getElementById('my_picture');
        image.src = "data:image/jpeg;base64," + imageData;
        self.imageData = imageData;
        //        navigator.notification.alert(imageData, null);
      }

      function onFail(message) {
        navigator.notification.alert('Failed because: ' + message);
      }

      //      navigator.notification.alert('finally making some progress');
      return false;
    },

    upload: function(imageURI) {
    },

    uploadPhoto: function (imageURI) {
      navigator.notification.alert("whoa baby", null);
      console.log(imageURI);

      function win(r) {
        console.log("Code = " + r.responseCode);
        console.log("Response = " + r.response);
        console.log("Sent = " + r.bytesSent);}


      function fail(error) {
        alert("An error has occurred: Code = " + error.code);
        console.log("upload error source " + error.source);
        console.log("upload error target " + error.target);}


      var uri = encodeURI("https://api.parse.com/1/files/pic.jpg");

      var options = new FileUploadOptions();
      options.fileKey="file";
      options.fileName=fileURI.substr(fileURI.lastIndexOf('/')+1);
      options.mimeType="image/jpeg";

      var headers = {
        'X-Parse-Application-Id': 'GejzV5ros8VQQleYBVpXRWhlSw4nZMmRbevIpI1g',
        'X-Parse-REST-API-Key': 'J0GNoC5iJT3dF0FRdYmq8oLm44Xi5lIHmaFsxUrT',
        'Content-Type': 'image/jpeg'
      }

      options.headers = headers;

      var ft = new FileTransfer();
      ft.upload(imageURI, uri, win, fail, options);

      return false;
    }
  });
}).call(this)
