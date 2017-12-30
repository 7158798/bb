$(function(){
    var type = 1;
    $(".coin_title").find("li").on("mouseover", function(){
        var _index=$(this).index();
        $(".coin_list").find("li").eq(_index).show().siblings().hide();
        $(this).addClass("cur").siblings().removeClass("cur");
        type = $(".coin_title li.cur").data("type");
    });

    $(".item_fir .item_leap i").on("click", function(){
        $(".item_fir .item_leap i.c_blue").removeClass("c_blue").addClass("c_gray");
        var _this = $(this);
        _this.removeClass("c_gray").addClass("c_blue");
        var sort = _this.data("sort");
        loadDatas(sort);
    });

    $(".item_fir .item_leap span.pointer").on("click", function(){
        var _this = $(this);
        var _cur = _this.next("span").find("i.c_blue");
        var _sort = '';
        if(_cur && _cur.length > 0){
            _sort = _cur.data("sort");
            var i = _cur.index();
            if(0 == i){
                _sort = _this.next("span").find("i:eq(1)").data("sort");
            }else{
                _sort = _this.next("span").find("i:eq(0)").data("sort");
            }
        }else{
            _sort = _this.next("span").find("i:eq(1)").data("sort");
        }

        _this.closest(".item_fir").find("i.c_blue").removeClass("c_blue").addClass("c_gray");
        _this.closest(".item_fir").find("i[data-sort='" + _sort + "']").removeClass("c_gray").addClass("c_blue");
        loadDatas(_sort);
    });

    function showPriceTrend(i){
        if(!i){
            $.get('/price/trend.html', {}, function(data){
                if(data && {} != data){
                    try{
                        $(".coin_list div.trend").each(function(){
                            var $this = $(this);
                            var trend = data[$this.data('id')];
                            $.plot($this, [trend], { grid: { borderWidth: 0}, xaxis: {ticks: false}, yaxis : {tickDecimals: 0, ticks: false},colors:['#fe741f'],shadowSize:0,lines:{lineWidth:1.5}});
                            $this.removeAttr('data-trend');
                        });
                    }catch(e) {
                        console.log(e);
                    }
                }
            }, 'json');
        }else{
            $.get('/price/trend.html', {}, function(data){
                if(data && {} != data){
                    try{
                        $("#dealList" + i + " div.trend").each(function(){
                            var $this = $(this);
                            var trend = data[$this.data('id')];
                            $.plot($this, [trend], { grid: { borderWidth: 0}, xaxis: {ticks: false}, yaxis : {tickDecimals: 0, ticks: false},colors:['#fe741f'],shadowSize:0,lines:{lineWidth:1.5}});
                        });
                    }catch(e) {
                        console.log(e);
                    }
                }
            }, 'json');
        }
    }
    // showPriceTrend();

    function loadDatas(sort){
        if(!sort) sort = 0;
        $.get("/coin/index.html", {sort : sort, type : type}, function(data){
            data = $.trim(data);
            if(data){
                if(1 == type){
                    $("#dealList1").html(data);
                }else if(2 == type){
                    $("#dealList2").html(data);
                }
                showPriceTrend(type);
            }
        }, "html");
    }
});