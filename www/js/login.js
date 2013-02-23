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
      'click #upload_picture': 'capturePhoto'
    },

    initialize: function() {
//      this.model.on('change', this.render, this);
//      this.model.on('destroy', this.render, this);
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('messageCreate'));
    },

    render: function () {
      $(this.el).append(this.template({ 'content': 'this content is json dynamic!', 'id': '1', 'notes': 'notes' }));
      return this;
    },

    capturePhoto: function() {
      navigator.camera.getPicture(onSuccess, onFail, { quality: 50,
           destinationType: Camera.DestinationType.DATA_URL});

      function onSuccess(imageData) {
        var image = document.getElementById('my_picture');
        image.src = "data:image/jpeg;base64," + imageData;
      }

      function onFail(message) {
        navigator.notification.alert('Failed because: ' + message);
      }

      navigator.notification.alert('finally making some progress');
      return false;
    }

  });
}).call(this)
