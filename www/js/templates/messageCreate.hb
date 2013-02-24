<h1>Message Create View</h1>

<h2>Upload a new message!</h2>

<button id="upload_picture">insert dick pic</button>

<img id="my_picture" src="" style="width: 100%;" />

<fieldset>
  <legend>
    Who do you want to see your message?
    <a href="#" data-task-id="{{id}}" class="pull-right delete-task
    btn"><i class="icon-trash"></i></a>
  </legend>
  <div class="control-group">
    <label for="task_title">Friends:</label>
    <input type="text" class="input-block-level" name="title"
    id="task_title" value="{{title}}" placeholder="The task's title">
  </div>
  <div class="control-group">
    <label for="task_notes">Public</label>
    <textarea class="input-block-level" name="notes" id="task_notes"
    placeholder="Notes about this task">{{notes}}</textarea>
  </div>
</fieldset>
<div class="form-actions">
  <button id="render_page_two" type="submit" class="btn btn-primary">Save Changes</button>
  <button class="cancel btn">Close</button>
</div>