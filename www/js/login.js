(function(){
	footprint.views.LoginView = Parse.View.extend({
	  events: {
//      "tap #login-button": "fbButtonTouch",
//	    "touchend #login-button": "fbButtonTouch",
	    "click #login-button": "fbButtonTouch", // for web browser support
	  },

		initialize: function() {
			this.template = Handlebars.compile(footprint.utils.templateLoader.get('login'));
			footprint.utils.fbInit();

	    // Additional initialization code such as adding Event Listeners goes here
		},

    render: function() {
      $(this.el).append(this.template());
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

	/**
	 *
	 */
  footprint.views.TabbedNavView = Parse.View.extend({
    id: 'tabbedNav',
    events: {},
    initialize: function() {
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('tabbedNav'));
      this.imageData = '';
    },

    render: function () {
      var _this = this;
      $(this.el).append(this.template({ 'content': 'this content is json dynamic!', 'id': '1', 'notes': 'notes' }));
      $(this.el).addClass('scroll');
      var timeout = setTimeout(function() {
        // we need to wait a split-second before we attach and activate the scrollbars
        _this.iscroll = new iScroll(_this.el, {hScrollbar:false,vScrollbar:false});
        clearTimeout(timeout);
      }, 200);

      return this;
    },
  });
}).call(this)
