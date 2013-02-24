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
      var timeout = setTimeout(function() {
        // we need to wait a split-second before we attach and activate the scrollbars
        footprint.iscroll.refresh();
        clearTimeout(timeout);
      }, 200);
      return this;
    },

  });
}).call(this);
