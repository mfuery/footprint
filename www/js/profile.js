(function(){
  footprint.views.ProfileView = Parse.View.extend({
    events: {
//      "click button": "fbButtonTouch", // for web browser support
    },

    fbuser: {
      id: "12345",
      first_name: "mike",
      last_name: "fury",
      pic: "img/logo/mikeprofile.jpg",
    },

    initialize: function() {
      var _this = this;
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('profile'));
//      footprint.utils.fbLogMeIn();

      // Retrieve metadata for profile from FB
//      FB.api('/me', function(response) {
//        _this.fbuser = response;
//        _this.render();
//      });
    },

    render: function () {
      var _this = this;
      $(this.el).append(this.template(this.fbuser));
      var timeout = setTimeout(function() {
        // we need to wait a split-second before we attach and activate the scrollbars
        _this.iscroll = new iScroll(_this.el, {hScrollbar:false,vScrollbar:false});
        clearTimeout(timeout);
      }, 200);

      return this;
    },

  });
}).call(this);
