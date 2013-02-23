(function(){
var MessageModel = Parse.Object.extend("Message");
//var myMessage = new MessageModel();
var query = new Parse.Query(MessageModel);
console.log(query)
query.get("5BSh4Gj4Ij", {
  success: function(messageObject) {
	  abc = messageObject
    console.log("found it", messageObject);
	  console.log("the content is: " + messageObject.get("content"));
  },
  error: function(obj, error) {
	  console.log("nothing", obj, error);
  }
});

/*
var testMessage = new Message();
testMessage.save({
	content: "This is a new message"
}, {
  success: function(object) {
    console.log("yay! it worked", object);
  }
});
*/

// login test

  window.fbAsyncInit = function() {
    // init the FB JS SDK
	Parse.FacebookUtils.init({
      appId      : '514654248576860', // App ID from the App Dashboard
      channelUrl : '//WWW.YOUR_DOMAIN.COM/channel.html', // Channel File for x-domain communication
      status     : true, // check the login status upon init?
      cookie     : true, // set sessions cookies to allow your server to access the session?
      xfbml      : true  // parse XFBML tags on this page?
    });
	FB.getLoginStatus(function(response) {
		  if (response.status === 'connected') {
		    // connected
		  } else if (response.status === 'not_authorized') {
		    // not_authorized
		  } else {
		    // not_logged_in
		  }
console.log("fb login status: ", response.status)
		 });

    // Additional initialization code such as adding Event Listeners goes here
	  Parse.FacebookUtils.logIn(null, {
		  success: function(user) {
		    if (!user.existed()) {
		      alert("User signed up and logged in through Facebook!");
		    } else {
		      alert("User logged in through Facebook!");
		    }
		  },
		  error: function(user, error) {
		    alert("User cancelled the Facebook login or did not fully authorize.");
		  }
		});

  };

  // Load the SDK's source Asynchronously
  // Note that the debug version is being actively developed and might
  // contain some type checks that are overly strict.
  // Please report such bugs using the bugs tool.
  (function(d, debug){
     var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement('script'); js.id = id; js.async = true;
     js.src = "//connect.facebook.net/en_US/all" + (debug ? "/debug" : "") + ".js";
     ref.parentNode.insertBefore(js, ref);
   }(document, /*debug*/ true));


  // Mailgun
  var Mailgun = require('mailgun');
  Mailgun.initialize('footprint.mailgun.org','key-2pern78kkrjgi1p1syldc826m6sm0665');


})();