(function() {
  footprint.views.MessageShowView = Parse.View.extend({

    events: {
    
    },

    initialize: function() {
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('message_show'));
    },

    render: function () {
      var self = this;

      var Message = Parse.Object.extend("Message");
      var query = new Parse.Query(Message);
      query.equalTo("recipients", "public");
      query.equalTo("objectId", "isWMjserzr");
      query.first({
        success: function(results) {
          $(self.el).append(self.template(results.toJSON()));          
          $(self.el).addClass('scroll');
          var timeout = setTimeout(function() {
            // we need to wait a split-second before we attach and activate the scrollbars
            self.iscroll = new iScroll(self.el, {hScrollbar:false,vScrollbar:false});
            clearTimeout(timeout);
          }, 200);

        },
        error: function(error) {
          alert("uh oh?");
        }
      });

      return this;
    }
  });
}).call(this)
