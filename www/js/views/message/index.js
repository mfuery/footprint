(function() {
  footprint.views.MessageIndexView = Parse.View.extend({
    tagName: 'ul',
    id: 'messages',

    events: {
    
    },

    initialize: function() {
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('message_index'));
    },

    render: function () {
      var self = this;

      var Message = Parse.Object.extend("Message");
      var query = new Parse.Query(Message);
      query.equalTo("recipients", "public");
      query.find({
        success: function(results) {
          _.each(results, function(msg) {
            $(self.el).append(self.template(msg.toJSON()));                      
          });
        },
        error: function(error) {
          alert("uh oh?");
        }
      });

      return this;
    },

    renderByLocation: function () {
      var self = this;

      $(self.el).append("<h1 style='padding: 10px 0; text-align: center; color: white; background: black;'>STARBUCKS</h1>");
      var Message = Parse.Object.extend("Message");
      var query = new Parse.Query(Message);
      query.equalTo("recipients", "public");
      query.equalTo("locationid", 1);
      query.find({
        success: function(results) {
          _.each(results, function(msg) {
            $(self.el).append(self.template(msg.toJSON()));                      
          });
        },
        error: function(error) {
          alert("uh oh?");
        }
      });

      return this;
    },

  });
}).call(this)
