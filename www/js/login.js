(function(){
	footprint.views.LoginView = Parse.View.extend({
	  events: {
//      "tap #login-button": "fbButtonTouch",
//	    "touchend #login-button": "fbButtonTouch",
	    "click #login-button": "fbButtonTouch", // for web browser support
	  },

		initialize: function() {
			this.template = Handlebars.compile(footprint.utils.templateLoader.get('login'));

			// todo uncomment me for facebook login
			//footprint.utils.fbInit();

	    // Additional initialization code such as adding Event Listeners goes here
		},

    render: function() {
      $(this.el).append(this.template());

      // Remove me (shortcircuit to dashboard)
      $('#main-navbar').after(new footprint.views.TabbedNavView().render().el);


      return this;
    },

		fbButtonTouch: function() {
		  console.log("fbButtonTouch");
      // Login to facebook button was clicked


      FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
          // connected
        } else if (response.status === 'not_authorized') {
          // not_authorized
        } else {
          Parse.FacebookUtils.logIn("", {
          success: function(user) {
            if (!user.existed()) {
              console.log("User signed up and logged in through Facebook!");
            } else {
              console.log("User logged in through Facebook!");
            }
            showAlert("User login success!", "SUCCESS");

            // Begin loading dashboard, via tabbed nav controls
            $('#content').after(new footprint.views.TabbedNavView().render().el);

          },
          error: function(user, error) {
            console.log(error);
            showAlert(error.message,error.code);
          }
        });
        }
        console.log("fb login status: ", response, response.status)
       });
		}

  });
}).call(this)
