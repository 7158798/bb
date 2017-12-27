$(function(){
    $("#add").on("click",function(){
        center_box("add_box");
        $("#tm_yy").show();
        $("#add_box").show();
    });
    $("#add_box").find(".confirm").click(function(){
        var id=$("#add_collection").find("option:selected").val();
        var param={"id":id};
        $.post("/account/add_collection.html",param,function(data){
          if(data.code==401){
              $("#login").trigger("click");
          }else if(data.code==200){
              location.href="/account/collection.html";
          }else{
              alert(data.msg);
          }
        },"json");
    });
    if($("#collections").find("li").length%4==1){
        $("#add").addClass("fir");
    }
    $("#collections").find("li").hover(function(){
        $(this).find(".delete").show();
    },function(){
        $(this).find(".delete").hide();
    });
    $("#collections").on("click",".delete",function(){
        var id=$(this).data("id");
        var param={"id":id};
        $.post("/account/cancel_collection.html",param,function(data){
            if(data.code==401){
                $("#login").trigger("click");
            }else if(data.code==200){
                location.href="/account/collection.html";
            }else{
                alert(data.msg);
            }
        },"json");
    });
});