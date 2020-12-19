//Allow to pick more than one option in genre and platform filter without using Ctrl
$(".advanced-select").mousedown(function(option){
    option.preventDefault();
    option.target.selected = !option.target.selected;
    
//Avoid scroll up when an option is picked
    var selectMultiple = this;
    var currentPlace = selectMultiple.scrollTop;
    setTimeout(function(){selectMultiple.scrollTop = currentPlace;}, 0);
});