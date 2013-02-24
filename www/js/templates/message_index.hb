<li class="message">
  <p>{{ message }}</p>
  {{#if image }}
    <div style="width: 100%; text-align: center;">
      <img src="{{ image.url }}" />
    </div>
  {{/if}}
  <p style="margin-bottom: 0; color: #777777; text-align: right;"><small>{{ createdAt }}</small></p>
</li>