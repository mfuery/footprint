
  <div class="row-fluid" style="width: 100%">
    <div style="width: 30%; float: left;">
      <div class="user-picture" style="margin: 10px 10px; background: white; height: 40px;">
        profoile
      </div>
    </div>
    <div class="span8" style="width: 70%; float: left;">
      <div class="user-profile" style="padding: 0px 10px;">
        <h4>Anthony Bui</h4>
        <h5>wearer of raybans</h5>
      </div>
    </div>
  </div>

  <div class="row-fluid">
    <div style="width: 100%">
      <h4 class="message-text">{{ message }}</h4>
      <small class="message-geopoint">Lat: {{ geopoint.latitude }}, Lon: {{ geopoint.longitude }}</small>
      <br>
      <small class="message-geopoint">{{ updatedAt }}</small>
      
      <div class="message-image" style="text-align: center">
        <img src="{{ image.url }}" style="width: 80%;">
      </div>
    </div>
  </div>

  <p>Recipients: {{ recipients }}</p>
