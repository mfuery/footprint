(function(){
	footprint.views.LoginView = Parse.View.extend({

		initialize: function() {
			this.template = Handlebars.compile(footprint.utils.templateLoader.get('login'));
		},

		render: function() {
			$(this.el).append(this.template());
		},


	})
}).call(this)