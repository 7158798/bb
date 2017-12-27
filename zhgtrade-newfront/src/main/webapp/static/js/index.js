$(function(){
    var placeholder = "手机验证码";
    var validate_type = "sms";

    function validatePhone(obj,type){
        var $this = $(obj);
        var flag = $this.data(_const.SUBMIT_FLAG);
        if(false == flag){
            $("#indexLoginTips2").text(_const.CAPTCHA_SECONDS+"秒内只能发送一次验证码！");
            return;
        }
        if($this.text()=="请求中"){
            $("#indexLoginTips2").text("请求中，请稍候！");
            return;
        }
        var code = $("#index_img_code").val();
        if(isEmpty(code)){
            $("#indexLoginTips2").text("请先填写图形验证码！");
            return;
        }
        if(code.length!=4){
            $("#indexLoginTips2").text("图形验证码错误");
            return;
        }
        $this.text("请求中");
        sendSmsCaptcha($this,type,code);
    }

    function sendSmsCaptcha(_btn,type,code){
        var _this = $(_btn);
        var id = _this.attr("id");
        var flag = _this.data(_const.SUBMIT_FLAG);
        if(false == flag){
            return;
        }
        _this.data(_const.SUBMIT_FLAG, false);
        var url;
        if(type=="sms"){
            url = '/captcha/sendSMSAuthCode';
        }
        if(type=="email"){
            url = '/captcha/sendEmailAuthCode';
        }
        if(type=="voice"){
            url = '/captcha/sendVoiceAuthCode';
        }
        $.post(url, {code:code, isLogin:"true"}, function(res){
            if('101' == res){
                $("#indexLoginTips2").text('请输入手机号');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('102' == res){
                $("#indexLoginTips2").text('非会员手机号');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('103' == res){
                $("#indexLoginTips2").text('您还没绑定手机号，去绑定');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('104' == res){
                $("#indexLoginTips2").text('手机号已存在');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('105' == res){
                $("#indexLoginTips2").text('验证码错误次数过多，请两小时后再试！');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('106' == res){
                $("#indexLoginTips2").text('图形验证码错误！');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('200' == res){
                _handler.addTipsHandler(_const.SMS_CAPTCHA_KEY, id);
                _handler.updateTipsSeconds(id, _const.CAPTCHA_SECONDS,1);
            }else{
                $("#indexLoginTips2").text('发送短信验证码失败');
                _this.data(_const.SUBMIT_FLAG, true);
            }
            _this.text("发送");
        }, 'json');
    }

    $("#index_image_code").click(function(){
        $(this).get(0).src = "/servlet/ValidateImageServlet?"+new Date().getTime();
    });
    $("#index_code").blur(function(){
        $("#indexLoginTips2").text("");
    });
    $("#index_img_code").blur(function(){
        $("#indexLoginTips2").text("");
    });
    $("#indexLoginName").blur(function(){
        $("#indexLoginTips1").text("");
    });
    $("#indexLoginPwd").blur(function(){
        $("#indexLoginTips1").text("");
    });

    $("#index_sendCode").on('click', function(){
        validatePhone(this,validate_type);
    });

    $("#index_sendVoiceCode").on('click', function(){
        validatePhone($("#index_sendCode"),'voice');
    });

    var index_login_contoller1 = true;
    var index_login_contoller2 = true;

    $("#index_login_btn1").click(function(){
        if(!index_login_contoller1){
            $("#indexLoginTips1").text("请求中，请稍后");
            return;
        }
        var loginName = $("#indexLoginName").val();
        var password = $("#indexLoginPwd").val();
        if(isEmpty(loginName)){
            $("#indexLoginTips1").html("用户名不能为空！");
            return;
        }
        if(password.length < 6){
            $("#indexLoginTips1").text("密码位数不小于6位数！");
            return;
        }
        index_login_contoller1 = false;
        $.post('/user/login',{password: password,loginName: loginName },function(result){
            if(result!=null){
                index_login_contoller1 = true;
                if(result.resultCode == 1){
                    jump(result);
                }else if(result.resultCode == -1){
                    $("#indexLoginTips1").html("用户名或密码错误");
                }else if(result.resultCode == -2){
                    $("#indexLoginTips1").html("此ip登录频繁，请2小时后再试");
                }else if(result.resultCode == -3){
                    if(result.errorNum == 0){
                        $("#indexLoginTips1").html("此ip登录频繁，请2小时后再试");
                    }else{
                        $("#indexLoginTips1").html("用户名或密码错误，您还有"+result.errorNum+"次机会");
                    }
                }else if(result.resultCode == -4){
                    $("#indexLoginTips1").html("请设置启用COOKIE功能")
                }else if(result.resultCode == 1){
                    window.location.href = document.getElementById("coinMainUrl").value;
                }else if(result.resultCode == 2){
                    $("#indexLoginTips1").html("账户出现安全隐患被冻结，请尽快联系客服");
                }else if(result.resultCode == -5){
                    $("#index_image_code").get(0).src = "/servlet/ValidateImageServlet?"+new Date().getTime();
                    $("#index_login_wrapper2").show();
                    $("#index_login_wrapper1").hide();
                    $("#indexLoginTips2").html("非常用设备，需要校验手机验证码");
                }else if(result.resultCode == -6){
                    $("#index_image_code").get(0).src = "/servlet/ValidateImageServlet?"+new Date().getTime();
                    $("#index_login_wrapper2").show();
                    $("#index_login_wrapper1").hide();
                    $("#index_voice_tips").hide();
                    placeholder = "邮箱验证码";
                    $("#index_code").attr("placeholder" , placeholder);
                    validate_type = "email";
                    $("#indexLoginTips2").html("非常用设备，需要校验邮箱验证码");
                }
            }
        },"json");
    });

    $("#index_login_btn2").click(function(){
        if(!index_login_contoller2){
            $("#indexLoginTips2").text("请求中，请稍后");
            return;
        }
        var phoneCode = $("#index_code").val();
        if(phoneCode.length != 6){
            $("#indexLoginTips2").text("验证码位数不符");
            return;
        }
        index_login_contoller2 = false;
        $.post('/user/login',{phoneCode: phoneCode}, function(res){
            index_login_contoller2 = true;
            if (res.resultCode == 1) {
                jump(res);
            }else if (res.code == "-15") {
                $("#indexLoginTips2").text("您没有发送验证码");
            } else if (res.code == "111") {
                $("#indexLoginTips2").text("验证码错误！剩余验证次数"+res.leftCount+"次");
            }
        },"json");
    });

    $(".consult_hd").find("li").mouseenter(function(){
        var $this = $(this);
        var _index = $this.index();
        $this.addClass("on").siblings().removeClass("on");
        if ($this.data("name")=="market"){
            if($this.data("market")!=1){
                getBTCMarket();
                $this.data("market",1);
            }
        }
        $(".consult_bd").find("li").eq(_index).show().siblings().hide();
    });
    function getBTCMarket(){
        $.get('/index/conjuncture', function (msg) {
            var len = msg.length;
            var data = [];
            for(var k = 0;k<len;k++){
                var arr = msg[k];
                data[k]=[arr[0]*1,arr[1]*1];
            }
            $('#container').highcharts({
                chart: {
                    zoomType: 'x'
                },
                title: {
                    text: '比特币（BTC）市场行情'
                },
                subtitle: {
                    text: document.ontouchstart === undefined ?
                        '点击查看详情' : '拖曳可以放大'
                },
                xAxis: {
                    type: 'datetime',
                    tickInterval:6*60*60*1000
                },
                yAxis: {
                    title: {
                        text: '成交价（BTC）'
                    }
                },
                legend: {
                    enabled: false
                },
                plotOptions: {
                    area: {
                        fillColor: {
                            linearGradient: {
                                x1: 0,
                                y1: 0,
                                x2: 0,
                                y2: 1
                            },
                            stops: [
                                [0, Highcharts.getOptions().colors[0]],
                                [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                            ]
                        },
                        marker: {
                            radius: 2
                        },
                        lineWidth: 1,
                        states: {
                            hover: {
                                lineWidth: 1
                            }
                        },
                        threshold: null
                    }
                },

                series: [{
                    type: 'area',
                    name: '收盘价',
                    data: data
                }]
            });
        },"json");
    }
    /*众股资讯-资讯板块*/
    // $.get("/index/consult",{size:"5"},function(data){
    //     var html = "";
    //     var $data = data.consult;
    //     var consult_length = $data.length;
    //     for(var i = 0;i<consult_length;i++){
    //         html+='<a href="http://news.zhgtrade.com/details.html?id='+$data[i].id+'" target="_blank"><div class="leap">'+
    //             '<img src="'+cdn+$data[i].imgPath+'?x-oss-process=image/resize,h_60,w_85,m_fill" class="db fl" alt="" />'+
    //             '<div class="content fl">'+
    //             '<p class="title ellipsis">'+$data[i].title+'</p>'+
    //             '<p class="txt">'+
    //             '<span class="db fl time">'+new Date($data[i].createdTime).format("MM-dd")+'</span>'+
    //             '<span class="db fr comment">'+
    //             '   <i class="db fl icon iconfont" href="javascript:void(0)" title="阅读量">&#xe63b;</i>'+
    //             '   <span class="db fl">'+$data[i].readingNum+'</span>'+
    //             '</span>'+
    //             '</p>'+
    //             '</div>'+
    //             '</div></a>';
    //     }
    //     $("#consult").html(html);
    //     var $pro_data = data.pro;
    //     var pro_length = $pro_data.length;
    //     var pro_html = "";
    //     for(var i = 0;i<pro_length;i++){
    //         pro_html+='<a time="'+$pro_data[i].startTime+'" current="'+new Date().getTime()+'" href="http://news.zhgtrade.com/road-show.html?id='+$pro_data[i].id+'" target="_blank"><div class="leap">'+
    //             '<img src="'+cdn+$pro_data[i].imgPath+'?x-oss-process=image/resize,h_60,w_85,m_fill" class="db fl" alt="" />'+
    //             '<div class="content fl">'+
    //             '<p class="title ellipsis">'+$pro_data[i].title+'</p>'+
    //             '<p class="txt">'+
    //             '<span class="db fl time">'+new Date($pro_data[i].startTime).format("yyyy-MM-dd HH:mm")+'</span>'+
    //             '<span class="db fr comment">';
    //         if($pro_data[i].startTime>new Date().getTime()){
    //             pro_html+='<i class="db road_icon roaded_icon"></i>';
    //         }else{
    //             pro_html+='<i class="db road_icon roading_icon"></i>';
    //         }
    //         pro_html+='</span>'+
    //             '</p>'+
    //             '</div>'+
    //             '</div></a>';
    //     }
    //     $("#pro").html(pro_html);
    // },"json");
    var $allItems = $('.carousel .carousel-inner .item');
    var $allIndicators = $('.carousel .carousel-indicators li');
    var currentIndex = 0;
    var currentItem = null;
    var nextItem = null;
    var time;
    $(".carousel").hover(function(){time = window.clearInterval(time)},function(){
        time = setInterval(function () {
            currentItem = $allItems.filter('.active');
            if (currentIndex + 1 === $allItems.length) {
                nextItem = $allItems.eq(0);
                currentIndex = 0;
            } else {
                nextItem = $allItems.eq(currentIndex + 1);
                currentIndex += 1;
            }
            nextItem.addClass('active').fadeIn(500);
            $allIndicators.removeClass('on').eq(currentIndex).addClass('on');
            currentItem.removeClass('active').fadeOut(1000);
        }, 5000);
    }).trigger("mouseleave");
    $(".carousel-indicators li").click(function(){
        var nextIndex =parseInt($(this).attr('data-to'));
        if (nextIndex == currentIndex)return false;
        currentIndex = nextIndex;
        currentItem = $allItems.filter('.active');
        currentItem.removeClass('active').fadeOut(1000);
        $allItems.eq(currentIndex).addClass('active').fadeIn(500);
        $allIndicators.removeClass('on').eq(currentIndex).addClass('on');
    });

    function showPriceTrend(){
        $.get('/price/trend.html', {}, function(data){
            if(data && {} != data){
                try{
                    $("#dealList div.trend").each(function(){
                        var $this = $(this);
                        var trend = data[$this.data('id')];
                        $.plot($this, [trend], {animate: true,grid: { borderWidth: 0}, xaxis: {ticks: false}, yaxis : {tickDecimals: 0, ticks: false},colors:['#fe741f'],shadowSize:0,lines:{lineWidth:1.5},});
                    });
                }catch(e) {
                    console.log(e);
                }
            }
        }, 'json');
    }
    $("#dealList").on("mouseenter",".chart_pic",function(){
        $(this).closest(".item_leap").find(".trend").show();
    });
    $("#dealList").on("mouseleave",".chart_pic",function(){
        $(this).closest(".item_leap").find(".trend").hide();
    });

    showPriceTrend();

    function loadDeals(sort){
        $.get("/index/dealDatas.html", {sort : sort}, function(data){
            data = $.trim(data);
            if(data){
                $("#dealList").html(data);
                showPriceTrend();
            }
        }, "html");
    }

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

        $(".item_fir .item_leap i.c_blue").removeClass("c_blue").addClass("c_gray");
        $(".item_fir .item_leap i[data-sort='" + _sort + "']").removeClass("c_gray").addClass("c_blue");
        loadDeals(_sort);
    });

    $(".item_fir .item_leap i").on("click", function(){
        $(".item_fir .item_leap i.c_blue").removeClass("c_blue").addClass("c_gray");
        var _this = $(this);
        _this.removeClass("c_gray").addClass("c_blue");
        var _sort = _this.data("sort");
        loadDeals(_sort);
    });
});