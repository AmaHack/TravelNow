Aria.tplScriptDefinition({
    $classpath : "app.templates.details.LayoutScript",
	$constructor: function(){
		var myScroll;
	},    
	$prototype : {

        $displayReady: function(){
        	$(".mask, .fb, .ses, .well, .popUp").hide();
			myScroll = new iScroll('wrapper');
			document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
			document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 200); }, false);
		}

    }

});