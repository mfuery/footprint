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
    }
  });
}).call(this)
