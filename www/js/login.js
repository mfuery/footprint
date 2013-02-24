(function(){
	footprint.views.LoginView = Parse.View.extend({
	  events: {
	    "touchend button": "fbButtonTouch",
	    "click button": "fbButtonTouch", // for web browser support
	  },

		initialize: function() {
			this.template = Handlebars.compile(footprint.utils.templateLoader.get('login'));

	    // init the FB JS SDK
			// https://github.com/phonegap/phonegap-facebook-plugin
      Parse.FacebookUtils.init({
        appId      : '514654248576860', // App ID from the App Dashboard
        nativeInterface: CDV.FB,
//        useCachedDialogs: false,
//        channelUrl : '//WWW.YOUR_DOMAIN.COM/channel.html', // Channel File for x-domain communication
//        status     : true, // check the login status upon init?
//        cookie     : true, // set sessions cookies to allow your server to access the session?
//        xfbml      : true  // parse XFBML tags on this page?
      });

	    // Additional initialization code such as adding Event Listeners goes here
		  //this.fbScriptLoader.call(this);
		},

    render: function () {
      $(this.el).append(this.template());
      return this;
    },

		fbButtonTouch: function() {
      // Login to facebook button was clicked
      Parse.FacebookUtils.logIn("", {
        success: function(user) {
          if (!user.existed()) {
            console.log("User signed up and logged in through Facebook!");
          } else {
            console.log("User logged in through Facebook!");
          }
          showAlert("User login success!", "SUCCESS");
        },
        error: function(user, error) {
          showAlert("User canceled the Facebook login or did not fully authorize.");
        }
      });
		}

	});

	footprint.views.MessageCreateView = Parse.View.extend({

    events: {
      'click #upload_picture': 'capturePhoto',
      'click #render_page_two': 'renderPageTwo',
      'click #upload_message': 'submit'
    },

    initialize: function() {
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('messageCreate'));
    },

    render: function () {
      $(this.el).append(this.template({ 'content': 'this content is json dynamic!', 'id': '1', 'notes': 'notes' }));
      return this;
    },

    submit: function() {
      var message_text = this.$el.find("input[name='text']").val();
      var message_recipients = this.$el.find("input[name='recipients']").val();

      var Message = Parse.Object.extend("Message");
      var message = new Message();

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
      var second_template = Handlebars.compile(footprint.utils.templateLoader.get('messageEdit'));
      $(this.el).html(second_template({ title: 'hello world', id: 'goodbye world' }));

      return false;
    },

    capturePhoto: function() {
      navigator.camera.getPicture(onSuccess, onFail, { quality: 50,
           destinationType: Camera.DestinationType.FILE_URI, sourceType: source }); 

      function onSuccess(imageURI) {
        var image = document.getElementById('my_picture');
        image.src = imageURI;
        uploadPhoto(imageURI);
      }

      function onFail(message) {
        navigator.notification.alert('Failed because: ' + message);
      }

      navigator.notification.alert('finally making some progress');
      return false;
    },

    uploadPhoto: function (imageURI) {
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
