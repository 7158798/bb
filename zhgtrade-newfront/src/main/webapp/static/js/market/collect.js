$(function(){
    $("#collect").click(function(){
        var $this=$(this);
        var id=$("#symbol").val();
        var param={"id":id};
        var flag=$this.data("flag");
        var url;
        if(flag==1){
            url="/account/cancel_collection.html";
        }else{
            url="/account/add_collection.html";
        }
        $.post(url,param,function(data){
            if(data.code==401){
                $("#login").trigger("click");
            }else if(data.code==200){
                if(flag==1){
                    $this.removeClass("cur");
                    $this.find("span").text("收藏");
                    $this.data("flag", 0);
                }else {
                    $this.addClass("cur");
                    $this.find("span").text("已收藏");
                    $this.data("flag", 1);
                }
            }else{
                alert(data.msg);
            }
        },"json");
    });
});