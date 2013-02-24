(function(){
	footprint.utils.templateLoader = {
		templates: {},
		load: function(names, callback) {
			var deferreds, _this = this;
			deferreds = [];
			$.each(names, function(index, name) {
				return deferreds.push($.get('js/templates/' + name + '.hb', function(data) {
					console.log('loaded');
					return _this.templates[name] = data;
				}));
			});
			return $.when.apply(null, deferreds).done(callback);
		},
		get: function(name) {
			var template;
			template = this.templates[name];
			if (!template) {
				showAlert('Template not loaded', 'Error');
			}
			return template;
		}
	};

	window.showAlert = function(message, title) {
		if (navigator.notification) {
			return navigator.notification.alert(message, null, title, 'OK');
		} else {
			return alert(title + ": " + message);
		}
	};

  footprint.utils.fbInit = function() {
    // init the FB JS SDK
    // https://github.com/phonegap/phonegap-facebook-plugin
    Parse.FacebookUtils.init({
      appId      : '514654248576860', // App ID from the App Dashboard
      nativeInterface: CDV.FB,
//      useCachedDialogs: false,
//      channelUrl : '//WWW.YOUR_DOMAIN.COM/channel.html', // Channel File for x-domain communication
//      status     : true, // check the login status upon init?
//      cookie     : true, // set sessions cookies to allow your server to access the session?
//      xfbml      : true  // parse XFBML tags on this page?
    });
  };

}).call(this);