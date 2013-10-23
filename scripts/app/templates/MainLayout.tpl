{Template {
  $classpath : "app.templates.MainLayout",
  $hasScript: true
}}

  {macro main()}
  <div id="wrapper">
    <div id="scroller">
      <ul id="thelist">
        <li class="img">
          {@embed:Placeholder {
          name : "header"
         }/}
        </li>
        <li class="headerTxt"> Inspirations </li>
        <li>{@embed:Placeholder {
          name : "content"
         }/}
         </li>
      </ul>
    </div>
  </div>
  <div id="footer">
    {@embed:Placeholder {
      name : "footer"
    }/}
  </div>
  {/macro}

{/Template}