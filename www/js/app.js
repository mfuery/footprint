(function() {
  window.footprint = {
    models: {},
    views: {},
    utils: {},
  };

  Parse.View.prototype.close = function() {
    if (this.beforeClose) {
      this.beforeClose();
    }

    if (this.iscroll) {
      console.log('destroying iscroll');
      this.iscroll.destroy();
      this.iscroll = null;
    }

    this.undelegateEvents();
    this.remove();
  };

  footprint.Router = Parse.Router.extend({

    routes: {
      '': "login",
      'test': "clickTest",
      'message_create': "createMessage",
      'profile':  "myProfile",
      'search': 'search',
      'dashboard': 'dashboard',
      'mapview' : 'mapview',
    },
    initialize: function() {
      var _this = this;
      this.pageHistory = [];

      $('#content').on('click', '.header-back-button', function(event) {
      	window.history.back();
      	return false;
      });

      if (document.documentElement.hasOwnProperty('ontouchstart')) {
      	document.addEventListener('touchmove', function(event) {
      	  event.preventDefault();
      	  return false;
      	});
      	$('#content').on('touchstart','a',function(event) {
      	  _this.selectItem(event);
      	});
      	$('#content').on('touchend','a', function(event) {
      	  _this.deselectItem(event);
      	})
      } else {
      	$('#content').on('mousedown','a', function(event) {
      	  this.selectItem(event);
      	});
      	$('#content').on('mousedown','a', function(event) {
      	  this.deselectItem(event);
      	})
      }
      // var loginView = new footprint.views.LoginView();
      // this.slidePage(loginView.render());

    },

    // var loginView = new footprint.views.LoginView();
    // $('#content').append(messageCreateView.el);

    selectItem: function(event) {
      $(event.target).addClass('tappable-active');
    },

    deselectItem: function(event) {
      $(event.target).removeClass('tappable-active');
    },

    login: function() {
      this.slidePage(new footprint.views.LoginView().render());
    },

    clickTest: function() {
      navigator.notification.alert('test click event', null);
    },

    createMessage: function() {
      this.slidePage(new footprint.views.MessageCreateView().render());
    },

    myProfile: function() {
      this.slidePage(new footprint.views.ProfileView().render());
    },

    search: function() {
      $(".navbar").append(new footprint.views.SearchView().render().el);
    },

    dashboard: function() {
      if (!this.dashboardPage) {
        this.dashboardPage = new footprint.views.TabbedNavView();
      }
      this.slidePage(this.dashboardPage.render());
    },

    mapview: function() {
      this.slidePage(new footprint.views.MapView().render());
    },

    slidePage: function(page) {
			var _this = this;
      // check is page is null if so no need to do render logic
      if (!page) {
        return null;
      }
			if (!this.currentPage){
				//$(page.el).attr('class','page stage-center');
				if (page === this.dashboardPage) {
          $('#main-navbar').after(page.el);
        } else {
          $('#content').append(page.el);
        }
				this.pageHistory = [window.location.hash];
				this.currentPage = page;
				return null;
			}
			if (this.currentPage !== this.dashboardPage) {
				this.currentPage.close();
			} else {
        $(this.currentPage.el).detach();
        // this.currentPage.rendered = false;
      }
			// $('.stage-right .stage-left').not('#dashboardPage').remove(); // probably not needed
			if (page === this.dashboardPage) {
				//$(page.el).attr('class', 'page stage-left');
				this.pageHistory = [window.location.hash];
        $('#main-navbar').after(page.el);
        this.currentPage = page;
        return null
			} else if (this.pageHistory.length > 1 && window.location.hash === this.pageHistory[this.pageHistory-2]) {
				$(page.el).attr('class', 'page stage-left');
				this.pageHistory.pop();
			} else {
				$(page.el).attr('class', 'page stage-right');
				this.pageHistory.push(window.location.hash);
			}
			$('#content').append(page.el);
			// setTimeout(function(){
			// 	$(_this.currentPage.el).attr('class', 'page transition stage-right'); // decides which direction slide either left or right
			// 	$(page.el).attr('class', 'page stage-center transition');
			// 	_this.currentPage = page;
			// });
			//$(_this.currentPage.el).attr('class', 'page transition stage-right'); // decides which direction slide either left or right
			//$(page.el).attr('class', 'page stage-center transition');
			_this.currentPage = page;
      return null;
		},
	});

}).call(this);
