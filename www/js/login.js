(function(){
	footprint.views.LoginView = Backbone.View.extend({
		
		events: function() {
			'tap': 'doThatTapThing'
		},

		render: function() {
			$('#content').append('something')
		},


	})
}).call(this)