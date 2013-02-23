(function() {
	var window.footprint = {
		models: {},
		views: {},
		utils: {},
	};

	Backbone.View.prototype.close = function() {
		if (this.beforeClose) {
			this.beforeClose();
		}
		this.undelegateEvents();
		this.remove(); 
	};
	footprint.Router = Backbone.Router.extend({

		routes: {
			'': 			"login", 
		},

		initialize: {
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
				} )
			}
		},

		selectItem: function(event) {
			$(event.target).addClass('tappable-active');
		}

		deselectItem: function(event) {
			$(event.target).removeClass('tappable-active');
		}

		login: function() {
			new LoginView({model:parse.User.Facebook})
		},

		slidePage: function(page) {
			var _this = this;
			if (!this.currentPage){
				$(page.el).attr('class','page stage-center');
				$('#content').append(page.el);
				this.pageHistory = [window.location.hash];
				this.currentPage = page;
			}
			if (this.currentPage !== this.dashboardPage) {
				this.currentPage.close();
			}
			$('.stage-right .stage-left').not('#dashboardPage').remove(); // probably not needed
			if (page === this.dashboardPage) {
				$(page.el).attr('class', 'page stage-left');
				this.pageHistory = [window.location.hash];
			} else if (this.pageHistory.length > 1 && window.location.hash === this.pageHistory[this.pageHistory-2]) {
				$(page.el).attr('class', 'page stage-left');
				this.pageHistory.pop();
			} else {
				$(page.el).attr('class', 'page stage-right');
				this.pageHistory.push(window.location.hash);
			}
			$('#content').append(page.el);
			setTimeout(function(){
				$(_this.currentPage.el).attr('class', 'page transition stage-right'); // decides which direction slide either left or right
				$(page.el).attr('class', 'page stage-center transition');
				_this.currentPage = page;
			});
		},
	})
})()