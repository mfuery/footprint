(function(){
	footprint.views.LoginView = Parse.View.extend({
    
    events: {
      'click': 'clickEvent',
    },

		initialize: function() {
			this.template = Handlebars.compile(footprint.utils.templateLoader.get('login'));
      navigator.notification.alert('new view', null);
		},

		render: function() {
			$(this.el).append(this.template());
			return this;
		},

    clickEvent: function(event) {
      console.log('touch');
      navigator.notification.alert('click event', null);
    }

	})
}).call(this)