<div class="sub-content">
  <h1 style="text-align: center;">New Message</h1>

  <img id="my_picture" src="" style="width: 100%;" />

  <div style="width: 100%">
    <!--<div style="width: 20%; float: left;">
      <a id="upload-picture" href="#camera">
        <i class="icon-camera" style="width:64px; height:64px; margin: 0 10px; background: blue;">PHOTO</i>
      </a>
    </div>-->
    <div style="width: 100%; float: left;">
      <div style="margin: 0 10px;">
        <textarea class="input-block-level" name="message_text" id="message_text"
          placeholder="Say something!" style="height: 200px;">{{ message }}</textarea>
      </div>
    </div>
  </div>

  <div style="margin: 0 10px;">
    <a id="upload-picture" href="#camera">
      <button class="btn pull-right">Add Media</button>
    </a>
  </div>

  <div style="clear: both;"></div>
  
  <form style="margin: 10px;">
    <fieldset>
      <legend style="font-size: 16px; color: white;">Who do you want to see your message?</legend>
      <label for="message_recipients">Add Friends:</label>
      <input type="text" id="message_recipients" name="recipients" placeholder="List your friends here…">
      <label class="checkbox">
        <input type="checkbox"> Everyone!
      </label>
      <button id="upload_message" type="submit" class="btn btn-primary pull-right">Save Changes</button>
    </fieldset>
  </form>
</div>