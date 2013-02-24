(function(){
  footprint.views.TabbedNavView = Parse.View.extend({
    id: 'tabbed-nav-group',
    events: {
      'click #home-btn'     : "clickHomeBtn",
      'click #connect-btn'  : "clickConnectBtn",
      'click #discover-btn' : "clickDiscoverBtn",
      'click #profile-btn'  : "clickProfileBtn",

    },
    initialize: function() {
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('tabbednav'));
      this.rendered = false;
    },

    render: function () {
      if (this.rendered){
        return null;
      } else {
        $(this.el).append(this.template());
        $(this.el).addClass("btn-group navbar-inverse navbar-fixed-top");
        return this;
      }
    },

    // event handlers

    clickCommon: function() {
    },

    clickHomeBtn: function() {
      this.clickCommon();
    },
    clickConnectBtn: function() {
      // todo facebook frens
    },
    clickDiscoverBtn: function() {
      this.clickCommon();
      footprint.app.navigate("#mapview");
      footprint.app.slidePage(new footprint.views.MapView().render());
    },
    clickProfileBtn: function() {
      this.clickCommon();
      footprint.app.navigate("#profile");
      footprint.app.slidePage(new footprint.views.ProfileView().render());
    },

  });

}).call(this);