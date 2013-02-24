(function(){
  footprint.views.ProfileView = Parse.View.extend({
    events: {
//      "touchend button": "fbButtonTouch",
//      "click button": "fbButtonTouch", // for web browser support
    },

//    fbuser: {
//      id: "",
//      name: "",
//      pic: "",
//
//    },

    initialize: function() {
      var _this = this;
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('profile'));
      footprint.utils.fbLogMeIn();

      // Retrieve metadata for profile from FB
      FB.api('/me', function(response) {
        _this.fbuser = response;
        _this.render();
      });
    },

    render: function () {
      $(this.el).append(this.template(this.fbuser));
      return this;
    },

  });
}).call(this);
