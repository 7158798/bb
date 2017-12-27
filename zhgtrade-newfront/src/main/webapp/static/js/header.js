$(function(){
    var curIndex = $("#menuIndex").val();
    $("#top_menubar li.current").removeClass("current");
    if(curIndex){
        $("#top_menubar li:eq(" + curIndex + ")").addClass("current");
    }
    
    var url = "account/"
    $.get()
});