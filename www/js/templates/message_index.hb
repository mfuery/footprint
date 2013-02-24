<li class="message">
  <p><a href="#message_show" style="color: black;">{{ message }}</a></p>
  {{#if image }}
    <div style="width: 100%; text-align: center;">
      <img src="{{ image.url }}" />
    </div>
  {{/if}}
  <p style="margin-bottom: 0; color: #777777; text-align: right;"><small>{{ createdAt }}</small></p>
</li>