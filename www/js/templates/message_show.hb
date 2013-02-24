
  <div class="row-fluid" style="width: 100%">
    <div style="width: 25%; float: left;">
      <div class="user-picture" style="margin: 10px 10px;">
        <img src="img/rex.jpg" />
      </div>
    </div>
    <div class="span8" style="width: 75%; float: left;">
      <div class="user-profile" style="padding: 0px 10px;">
        <h4 style="margin-bottom: 0; text-decoration: underline;">Anthony Bui</h4>
        <h5 style="margin-top: 0;">wearer of raybans &amp; brogrammer</h5>
      </div>
    </div>
  </div>

  <div style="width: 100%;">
    <div style="padding: 0 10px;">
      <h4 class="message-text">{{ message }}</h4>
      <small class="message-geopoint">Lat: {{ geopoint.latitude }}, Lon: {{ geopoint.longitude }}</small>
      <br>
      <small class="message-geopoint">{{ updatedAt }}</small>
      
      <div class="message-image" style="text-align: center">
        <img src="{{ image.url }}" style="width: 80%;">
      </div>
    </div>
  </div>
