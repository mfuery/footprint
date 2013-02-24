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
      // this.rendered = false;
      this.currentTab = null;
    },

    render: function () {
      if (this.rendered){
        return null;
      } else {
        $(this.el).append(this.template());
        $(this.el).addClass("btn-group navbar-inverse navbar-fixed-top");
        // this.rendered = true;
        if (!this.currentTab) {
          this.currentTab = new footprint.views.MessageIndexView().render();
          $('#content').append(this.currentTab.el);
        }
        return this;
      }
    },

    // event handlers

    clickCommon: function() {
      this.currentTab.close()
    },

    clickHomeBtn: function() {
      console.log('home tab');
      this.clickCommon();
      this.currentTab = new footprint.views.MessageIndexView().render();
      $('#content').append(this.currentTab.el);
    },
    clickConnectBtn: function() {
      // todo facebook frens
    },
    clickDiscoverBtn: function() {
      console.log('map tab');
      this.clickCommon();
      this.currentTab = new footprint.views.MapView().render();
      $('#content').append(this.currentTab.el);
    },
    clickProfileBtn: function() {
      console.log('profile tab');
      this.clickCommon();
      this.currentTab = new footprint.views.ProfileView().render();
      $('#content').append(this.currentTab.el);
    },

  });

  footprint.views.HomeView = Parse.View.extend({
    initialize: function() {
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('home'));
    },
    render: function() {
      var _this = this;
      $(this.el).append(this.template());
      var timeout = setTimeout(function(){
        _this.iscoll = new iScroll('homeMsgList', {hScollbar:false,vScrollbar:false});
        clearTimeout(timeout);
      }, 200);
      return this;
    }
  });

}).call(this);