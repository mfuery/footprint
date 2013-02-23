(function(){
  footprint.views.LoginView = Parse.View.extend({

    initialize: function() {
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('login'));
    },

    render: function() {
      $(this.el).append(this.template());
      return this;
    },

    clickEvent: function(event) {
      console.log('touch');
      navigator.notification.alert('click event', null);
    }


  footprint.views.MessageCreateView = Parse.View.extend({
    
    events: {
      'click #upload_picture': 'capturePhoto'
    },

    initialize: function() {
//      this.model.on('change', this.render, this);
//      this.model.on('destroy', this.render, this);
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('messageCreate'));
    },
    
    render: function () {
      $(this.el).append(this.template({ 'content': 'this content is json dynamic!', 'id': '1', 'notes': 'notes' }));
      return this;
    },

    capturePhoto: function() {
      navigator.camera.getPicture(onSuccess, onFail, { quality: 50,
           destinationType: Camera.DestinationType.DATA_URL}); 

      function onSuccess(imageData) {
        var image = document.getElementById('my_picture');
        image.src = "data:image/jpeg;base64," + imageData;
      }


      function onFail(message) {
        alert('Failed because: ' + message);
      }
      
      alert('finally making some progress');
      return false;
    }

    
  });
}).call(this)
