var postButKeepInBacklogButton = document.getElementById("postButKeepInBacklogButton");
if(postButKeepInBacklogButton != null){
	postButKeepInBacklogButton.onclick=function(option){
	    document.getElementById("removeFromBacklogInput").value = false;
	    document.scores.submit();
	};
}

var postAndRemoveFromBacklogButton = document.getElementById("postAndRemoveFromBacklogButton")
if(postButKeepInBacklogButton != null){
	postAndRemoveFromBacklogButton.onclick=function(option){
	    document.getElementById("removeFromBacklogInput").value = true;
	    document.scores.submit();
	};
}