<div class="sub-content">
  <h1>Other options</h1>
  <h2 class="pull-left">Compose</h2>
  <button id="render_page_two" type="submit" class="btn btn-primary pull-right">Save Changes</button>

  <img id="my_picture" src="" style="width: 100%;" />

  <table class="table" style="width:97%">
    <tr>
      <td style="width:64px"><img id="profile-pic" src="{{fbuser.picture}}" style="width:64px; height:64px;" />
      </td>
      <td><textarea class="input-block-level" name="notes" id="task_notes"
        placeholder="Write your message here" style="width:100%">{{notes}}</textarea>
      </td>
    </tr>
    <tr>
      <td>
        <a id="upload-picture" href="#camera">
          <i class="icon-camera" style="width:48px; height:48px;"></i>
        </a>
      </td>
      <td> Add a photo 
      </td>
    </tr>
  </table>

  <!-- TODO mini image + mini map -->
  <button class="upload_picture">insert dick pic</button>

  <fieldset>
    <legend>
      Who do you want to see your message?
    </legend>
    <div class="control-group">
      <label for="message_text">Message:</label>
      <input type="text" class="input-block-level" name="text"
      id="message_text" value="{{message}}" placeholder="Insert text message here...">
    </div>
    <div class="control-group">
      <label for="message_recipients">Friends:</label>
      <input type="text" class="input-block-level" name="recipients"
      id="message_recipients" value="{{recipients}}" placeholder="Recipient ID">
    </div>
  </fieldset>
</div>