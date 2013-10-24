{Template {
  $classpath : "app.templates.home.HomeLayout",
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
  <div class="popUp">
  {@embed:Placeholder {
      name : "popup"
    }/}
  </div>
  <div class="mask"></div>
  {/macro}

{/Template}