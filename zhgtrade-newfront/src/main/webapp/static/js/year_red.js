/*<!--新年红包重写url-->*/
(function ($) {
    /*判断参数*/
    function GetQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)return unescape(r[2]);
        return null;
    }
    var _ajax = $.ajax;
    $.ajax = function (opt) {
        if(opt.url.indexOf("/market/buyBtcSubmit")!=-1||opt.url.indexOf("/market/sellBtcSubmit")!=-1){
            var fn = {success:function(data, textStatus){}};
            if(opt.success){fn.success=opt.success;}
            opt.data['url'] = opt.url;
            opt.data['year_secret'] = GetQueryString("year_secret");
            opt.url = "/year/red.html";
            var _opt = $.extend(opt, {
                success:function(data, textStatus){
                    fn.success(data, textStatus);
                    if(data && data.code==401){
                        //location.href='/users/login.html';
                    }else if(data.code==200){
                        if(data.amount!=0) {
                            var html = '<div class="new_year_red" id="new_year_red_1">' +
                                '    <img class="bg bg1" src="//zhgtrade.oss-cn-qingdao.aliyuncs.com/images/new_year_red/bg1.png"" alt="">' +
                                '    <div class="year_red_content">' +
                                '    <!--<a href="javascript:void(0)" class="iconfont close">&#xe632;</a>-->' +
                                '<img class="break" id="open_year_red" src="//zhgtrade.oss-cn-qingdao.aliyuncs.com/images/new_year_red/open.png"" alt="">' +
                                '    <p class="open">点击拆开</p>' +
                                '    <p class="desc">恭喜您获得由比特家</p>' +
                                '    <p class="desc">提供的0.01~100元现金红包</p>' +
                                '</div>' +
                                '</div>' +
                                '<div class="new_year_red dn" id="new_year_red_2">' +
                                '    <img class="bg bg2" src="//zhgtrade.oss-cn-qingdao.aliyuncs.com/images/new_year_red/bg2.png"" alt="">' +
                                '    <div class="year_red_content">' +
                                '    <a href="javascript:void(0)" class="iconfont close">&#xe677;</a>' +
                                '<p class="detail fir">恭喜您获得由比特家</p>' +
                                '    <p class="detail">提供的<span>' + data.amount + '元</span>红包</p>' +
                                '<p class="res">您的红包已放入您的</p>' +
                                '    <p class="res">比特家账户余额中，请查收</p>' +
                                '</div>' +
                                '</div><div class="year_bg" id="year_bg"></div>';
                            $("body").append(html);
                            $("#open_year_red").click(function () {
                                var $this = $(this);
                                $this.addClass("on");
                                setTimeout(function () {
                                    $("#new_year_red_2").show();
                                    $("#new_year_red_1").hide();
                                }, 500);
                            });
                            $("#new_year_red_2").find(".close").click(function () {
                                $("#new_year_red_2").remove();
                                $("#new_year_red_1").remove();
                                $("#year_bg").remove();
                            });
                        }
                    }
                }
            });
        }else{
            var _opt = opt
        }
        return _ajax(_opt);
    };
})(jQuery);