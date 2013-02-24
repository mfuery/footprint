(function(){
  footprint.views.MapView = Parse.View.extend({
    id: 'mapview',
    //events: {},
    initialize: function() {
      //this.template = Handlebars.compile(footprint.utils.templateLoader.get('tabbednav'));
console.log("mapview")
    },

    render: function () {
      navigator.geolocation.getCurrentPosition(this.geoSuccess, this.geoError);
      return this;
    },


    // callbacks
    geoSuccess: function(position){
      this.glatlon = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
      this.gmap = new google.maps.Map(document.getElementById("mapview"), {
        center: this.glatlon,
        zoom: 18,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      });
      var youAreHereMarker = new google.maps.Marker({
          position: this.glatlon,
          map: this.gmap,
          title: "You Are Here"
      });
    },
    geoError: function(positionError){
      console.log('error code: ' + positionError.code + " message: " + positionError.message);
      navigator.notification.alert("GeoError :(", null);
    },

    beforeClose: function() {
      this.gmap = null;
      this.glatlon = null;
    },


    // private members
    gmap: null,
    glatlon: null,
    markers: [],


    // event handlers

    clickCommon: function() {
    },

    clickHomeBtn: function() {
      this.clickCommon();
    },

  });

}).call(this);