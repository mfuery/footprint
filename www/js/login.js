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
			// footprint.utils.fbInit();

	    // Additional initialization code such as adding Event Listeners goes here
		},

    render: function() {
      var _this = this;
      FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
          console.log('navigate to dashboard');
          footprint.Router.navigate('dashboard', {trigger:true});
        } else if (response.status === 'not_authorized') {
          // not_authorized
        } else {
          $(_this.el).append(_this.template());
        }
        
        console.log("fb login status: ", response, response.status)
       });

      // Remove me (shortcircuit to dashboard)
      // $('#main-navbar').after(new footprint.views.TabbedNavView().render().el);

      return this;
    },

		fbButtonTouch: function() {
		  console.log("fbButtonTouch");
      // Login to facebook button was clicked

          Parse.FacebookUtils.logIn("email", {
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

  });
}).call(this)
