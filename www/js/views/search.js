(function() {
  footprint.views.SearchView = Parse.View.extend({
    id: 'search_bar',

    events: {
      'submit': 'submit'
    },

    initialize: function() {
      this.template = Handlebars.compile(footprint.utils.templateLoader.get('search'));
    },

    render: function () {
      var _this = this;

      $(this.el).append(this.template());

      return this;
    },

    submit: function() {
      navigator.notification.alert('perform search', null);
    }
  });
}).call(this);
