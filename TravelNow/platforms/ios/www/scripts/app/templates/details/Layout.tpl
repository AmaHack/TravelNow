{Template {
  $classpath : "app.templates.details.Layout",
  $hasScript: true
}}

  {macro main()}
   <div id="header">
    <div class="backButton"><a href="index.html"></a></div>
  </div>
  <div id="wrapper" class="detailsWrapper">
    <div id="scroller">
      <ul id="thelist">
        <li class="maps">
        {@embed:Placeholder {
            name : "maps"
        }/}
        </li>
        <li class="placeDetails">
          <div class="container">
            <div class="header">Place details</div>
            <div class="placeDescription">
              <div class="content">
              {@embed:Placeholder {
                name : "place_details"
              }/}
              </div>
            </div>
          </div>
        </li>
        <li class="placeDetails events">
          <div class="container">
            {@embed:Placeholder {
                name : "events"
              }/}            
           </div>
        </li>
      </ul>
    </div>
  </div>             
  <div id="footer">
     <div class="button"  onClick="window.open('search.html', '_self')">Book</div>
  </div>
  <div class="mask"></div>

  {/macro}

{/Template}