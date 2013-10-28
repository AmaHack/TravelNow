Aria.tplScriptDefinition({
    $classpath : "app.templates.home.HomeLayoutScript",
	$constructor: function(){
		var myScroll;
	},    
	$prototype : {

        $displayReady: function(){
        	$(".mask, .dialog, .popUp").hide();
			
			$(".close").click(function(){
				$(".mask, .dialog, .popUp").hide();
			});			

			myScroll = new iScroll('wrapper');
			document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
			document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 200); }, false);
		},
		facebookData: function(){
			var myData = {};
			myData.title = "Inspired by friends";
			myData.desc = "The top three locations where several of your Facebook friends have recently enjoyed visiting, and we’re offering you great deals and offers to try it yourself";
			myData.list = new Array();
			myData.list[0] = {};
			myData.list[0].title = "Paris, France";
			myData.list[0].desc = "visitied by 15 of your friends";
			myData.list[1] = {};
			myData.list[1].title = "New Delhi,, India";
			myData.list[1].desc = "visitied by 25 of your friends";
			this.$json.setValue(this.data,"popupData",myData);

			$(".loading, .mask").show();
			$(".loading").hide();
			$(".popUp, .dialog").show();
		},
		seasonalData: function(){
			var myData = {};
			myData.title = "Seasonal inspirations";
			myData.desc = "We know it is the time of the year, when you have an opportunity to take a break from your hectic life and escape to an exotic or tranquil places. Here is a great list of offers for such places to visit.";
			myData.list = new Array();
			//myData.list[0] = {};
			this.$json.setValue(this.data,"popupData",myData);

			$(".loading, .mask").show();
			$(".loading").hide();
			$(".popUp, .dialog").show();
		},
		wellbeingData: function(){
			var myData = {};
			myData.title = "Well being inspirations";
			myData.desc = "It seems you are likely to seek holidays with a specific focus, for example, wellbeing/medical tourism, learning/cultural holidays and ethical voyages. Here is a list of such great occasions which you would like to consider.";
			myData.list = new Array();
			//myData.list[0] = {};
			this.$json.setValue(this.data,"popupData",myData);

			$(".loading, .mask").show();
			$(".loading").hide();
			$(".popUp, .dialog").show();
			$(".mask, .popUp, .dialog").show();
		}

    }

});