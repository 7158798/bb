(function(window, document, $){
    var timer;
    $("#button").on("click",function(){
        $("#message").fadeIn();
        timer && clearTimeout(timer)
        timer=setTimeout(function(){
            $("#message").fadeOut(400);
        },600);
    });
    new ZeroClipboard(document.getElementById("button"));
    var url = $("#content").val();
    window._bd_share_config={
        "common":{
            "bdSnsKey":{},
            "bdText":"比特家，安全可靠的数字货币交易网站，注册赢精彩",
            "bdMini":"3",
            "bdSign":"off",
            "bdUrl":url,
            "bdMiniList":[
                "qzone",
                "tsina",
                "bdysc",
                "weixin",
                "renren",
                "tqq",
                "bdxc",
                "kaixin001",
                "tqf",
                "tieba",
                "douban",
                "bdhome",
                "sqq",
                "thx",
                "ibaidu",
                "meilishuo",
                "mogujie",
                "diandian",
                "huaban",
                "duitang",
                "hx",
                "fx",
                "youdao",
                "sdo",
                "qingbiji",
                "people",
                "xinhua",
                "mail",
                "isohu",
                "yaolan",
                "wealink",
                "ty",
                "iguba",
                "fbook",
                "twi",
                "linkedin",
                "h163",
                "evernotecn",
                "copy",
                "print"],
            "bdPic":resources+"/static/images/intro/tuiguang2.jpg",
            "bdStyle":"0",
            "bdSize":"32"
        },
        "share":{}
    };
    with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src= resources + '/statics/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];

    function toggleContent(index, refresh, params){
        if(!refresh && $.trim(this.html())){
            return;
        }

        if(!params){
            params = '';
        }

        switch(index){
            case 1:
                this.load('/account/intros.html?' + params);
                break;
            case 2:
                this.load('/account/incomes.html?' + params);
                break;
            case 3:
                this.load('/account/topn.html');
                break;
            default:
                break;
        }
    }

    $(function () {
        $("#title_lis li.db").on("click", function(){
            var $this = $(this);
            $("#title_lis li.cur").removeClass('cur');
            $this.addClass('cur');

            var index = $this.data('index');
            var $divs = $("#contents");
            $divs.find('.li_item:visible').addClass('dn');

            var $item = $divs.find('.li_item:eq(' + index + ')');
            $item.removeClass('dn');
            toggleContent.apply($item, [index]);
        });

        $("#contents .Tenbody").on('click', '.page a', function (event) {
            var $this = $(this);
            var $item = $this.closest('div.li_item');
            var params = $.trim($this.attr('href').split('\?')[1]);
            event.preventDefault();
            return params && toggleContent.apply($item, [$item.index(), true, params]) && false;
        });
    });
    
})(window, document, jQuery);