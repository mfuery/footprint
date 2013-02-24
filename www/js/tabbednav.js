(function(){
  footprint.views.TabbedNavView = Parse.View.extend({
    id: 'tabbed-nav-group',
    events: {
      'click home-btn'     : "clickHomeBtn",
      'click connect-btn'  : "clickConnectBtn",
      'click discover-btn' : "clickDiscoverBtn",
      'click profile-btn'  : "clickProfileBtn",

    },
    initialize: function() {
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('tabbedNav'));
    },

    render: function () {
      $(this.el).append(this.template());
      return this;
    },


    // private members

    // call .close() method upon changing view
    currentView: null,


    // event handlers

    clickCommon: function() {
    },

    clickHomeBtn: function() {
      this.clickCommon();
    },
    clickConnectBtn: function() {
      // todo
    },
    clickDiscoverBtn: function() {
      // todo maps
    },
    clickProfileBtn: function() {
      this.clickCommon();
      footprint.Router.slidePage(new footprint.views.ProfileView().render());

    },

  });

}).call(this);