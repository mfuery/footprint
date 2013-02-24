<h1>Other options</h1>

<h2>{{ title }}</h2>

<!-- TODO mini image + mini map -->

<fieldset>
  <legend>
    Who do you want to see your message?
    <a href="#" data-message-id="{{id}}" class="pull-right delete-message
    btn"><i class="icon-trash"></i></a>
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
<div class="form-actions">
  <button id="upload_message" type="submit" class="btn btn-primary">Upload Message</button>
</div>