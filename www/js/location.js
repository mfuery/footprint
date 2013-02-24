(function(){
	footprint.locationService = {
		get: function() {
			var _this = this;
			navigator.geolocation.getCurrentPosition(_this.geoSuccess, _this.geoError);
		},
		geoSuccess: function(position){
			var client_id = 'WCZGXBNF5RGW0VPBUKSV5CLVT2WA5B505OCYIQE1UON21ZHE',
					client_secret = '3QCEZLSG1SSSIEN2IVRIPY1EIRBSMMKF5GIQNTQ5UR2G3HEN';
					data = {
						client_id: client_id,
						client_secret: client_secret,
						ll : position.coords.latitude+", "+position.coords.longitude,
						llacc : position.coords.accuracy,
						limit: 10,
					};
					$.get('https://api.foursquare.com/v2/venues/search', data, function(data, status, jqXHR) {
						console.log(status, data);
					});
		},
		geoError: function(positionError){
			console.log('error code: ' + positionError.code + " message: " + positionError.message);
		}
	}
}).call(this);