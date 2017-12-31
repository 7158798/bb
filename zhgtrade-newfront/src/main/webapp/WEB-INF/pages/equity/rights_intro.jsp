<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit'/>
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
    <style>
        li{cursor: auto;}
        .ml124{margin-left: 124px;}
        .banner_box{width: 100%;height: 900px;}
        .banner{
            position: relative;
            width: 100%;
            height: 100%;
            overflow: hidden;
            background: url("${resources}/static/images/equality/banner.jpg") no-repeat;
            background-position: 50% 50%;
            background-size: cover;
            -moz-background-size: cover;
            -webkit-background-size: cover;
        }
        .banner .banner_text{
            position: absolute;
            top: 50%;
            left: 50%;
            margin-left: -450px;
            margin-top: -92px;
            display: block;
        }
        .banner .banner_text li{
            cursor: auto;
        }
        .banner .banner_text li span{
            display: inline-block;
            width: 30px;
        }
        .banner .banner_text .title{
            font-family: "华文中宋";
            font-size: 75px;
            color: #FFF;
        }
        .banner .banner_text .sub_title{
            font-family: "华文细黑";
            font-size: 40px;
            color: #FFF;
            margin-top: 70px;
        }

        .coins_box{
            background: #FBFBFB;
            width: 100%;
            height: 900px;
        }
        .coins_box .coins_title{
            margin: 0 auto;
            text-align: center;
            line-height: 290px;
            font-size: 50px;
            font-family: "Microsoft YaHei";
            color: #ff9211;
        }
        .coins_box .block_box{
            width: 100%;
        }
        .coins_box .coin_block{
            width: 1517px;
            height: 397px;
            margin: 0 auto;
        }
        .coins_box .coin_block li{
            float: left;
            height: 397px;
            display: inline-block;
            cursor: auto;
            color: #333;
        }
        .coins_box .coin_block .text_box{
            margin-top: 33px;
            width: 222px;
            height: 310px;
            text-align: center;
            border: 2px solid #ff9211;
        }
        .coins_box .coin_block .text_box .content{
            padding: 20px;
            line-height: 30px;
            font-size: 20px;
            font-family: "Microsoft YaHei";
            text-align: left;
            color: #646464;
        }
        .coins_box .coin_block .title{
            display: block;
            width: 226px;
            padding: 10px 0;
            color: #fff;
            font-size: 30px;
            font-family: 黑体;
            background: #ff9211;
        }
        .coins_box .coin_block .center{
            display: block;
            width: 365px;
        }
        .coins_box .coin_block .center li{
            display: table-cell;
            width: 121.5px;
            text-align: center;
        }
        .coins_box .coin_block .center li p{
            font-family: "Microsoft YaHei";
            font-size: 60px;
            color: #646464;
            padding: 48.5px 10px;
        }
        .coins_box .coin_block .center .left{
            display: block;
            width: 79px;
            height: 23px;
            margin-top: 187px;
            margin-left: 42px;
            background: url("${resources}/static/images/equality/left.png") no-repeat;
            background-position: 50% 50%;
            background-size: cover;
            -moz-background-size: cover;
            -webkit-background-size: cover;
        }
        .coins_box .coin_block .center .right{
            display: block;
            width: 79px;
            height: 23px;
            margin-top: 187px;
            background: url("${resources}/static/images/equality/right.png") no-repeat;
            background-position: 50% 50%;
            background-size: cover;
            -moz-background-size: cover;
            -webkit-background-size: cover;
        }

        .feature_box{
            width: 100%;
            height: 900px;
            background: #FFF;
        }
        .feature_box .title_block{
            width: 100%;
            padding: 100px 0;
            text-align: center;
        }
        .feature_box .title_block li{
            font-family: "幼圆";
            font-size: 30px;
            color: #282828;
            padding: 30px 0;
        }
        .feature_box .title_block li span{
            font-size: 50px;
            padding: 20px 0;
            border-bottom: 1px solid #ff9211;
        }
        .feature_box .feature_block{
            width: 100%;
            margin-top: 20px;
        }
        .feature_box .feature_block ul{
            width: 1880px;
            margin: 0 auto;
            height: 340px;
        }
        .feature_box .feature_block li{
            float: left;
            position: relative;
            width: 455px;
            height: 340px;
            background: #f3f3f3;
            font-family: 华文细黑;
            display: inline-block;
            transition:all 0.5s;
            -moz-transition:all 0.5s;
            -webkit-transition:all 0.5s;
            -o-transition:all 0.5s;
        }
        .feature_box .feature_block li .num{
            position: absolute;
            left: 60px;
            top: -20px;
            width: 100px;
            height: 110px;
            line-height: 110px;
            background: #2d2d2d;
            text-align: center;
            font-size: 60px;
            color: #FFF;
            transition:all 0.5s;
            -moz-transition:all 0.5s;
            -webkit-transition:all 0.5s;
            -o-transition:all 0.5s;
        }
        .feature_box .feature_block li p{
            color: #464646;
            font-size: 22px;
            padding: 130px 30px;
            line-height: 30px;
            transition:all 0.5s;
            -moz-transition:all 0.5s;
            -webkit-transition:all 0.5s;
            -o-transition:all 0.5s;
        }
        .feature_box .feature_block li:hover{
            background: #2d2d2d;
        }
        .feature_box .feature_block li:hover .num{
            background: #ff9211;
            height: 130px;
            line-height: 130px;
        }
        .feature_box .feature_block li:hover p{
            color: #FFF;
            padding: 0 30px;
            padding-top: 140px;
            padding-bottom: 130px;
        }

        .mix_box{
            width: 100%;
            height: 900px;
            background: #fafafa;
            position: relative;
        }
        .mix_box .title_block{
            font-family: "Microsoft YaHei";
            font-size: 40px;
            padding: 60px 0;
        }
        .mix_box .title_block li{
            line-height: 76px;
        }
        .mix_box .title_block .btns{
            margin: 0 auto;
            height: 38px;
            width: 518px;
            margin-top: 56px;
        }
        .mix_box .title_block .btns span{
            display: inline-block;
            font-size: 16px;
            padding: 10px;
            width: 100px;
            height: 16px;
            line-height: 16px;
            float: left;
            text-align: center;
            color: #989898;
            border: 1px solid #d1d1d1;
        }
        .mix_box .title_block .btns span:hover{
            background: #ff9211;
            color: #FFF;
            border: 1px solid #ff9211;
        }
        .mix_box .imgs_block{
            width: 1879px;
            height: 510px;
            margin: 0 auto;
        }
        .mix_box .imgs_block .img{
            width: 613px;
            height: 452px;
        }
        .mix_box .imgs_block .img1{
            background: url("${resources}/static/images/equality/01.jpg") no-repeat;
            background-position: 50% 50%;
            background-size: cover;
            -moz-background-size: cover;
            -webkit-background-size: cover;
        }
        .mix_box .imgs_block .img2{
            background: url("${resources}/static/images/equality/02.jpg") no-repeat;
            background-position: 50% 50%;
            background-size: cover;
            -moz-background-size: cover;
            -webkit-background-size: cover;
        }
        .mix_box .imgs_block .img3{
            background: url("${resources}/static/images/equality/03.jpg") no-repeat;
            background-position: 50% 50%;
            background-size: cover;
            -moz-background-size: cover;
            -webkit-background-size: cover;
        }
        .mix_box .imgs_block .img span{
            clear: both;
            display: block;
            font-size: 18px;
            line-height: 58px;
            color: #323232;
            width: inherit;
            text-align: center;
            position: absolute;
        }
        .mix_box .imgs_block .img .img_mask{
            width: inherit;
            height: inherit;
            filter:alpha(opacity=50);
            /* -moz-opacity: 0.5;
            opacity: 0.5;*/
            background-color:rgba(0,0,0,0);
        }
        .mix_box .imgs_block .img .title1{
            bottom: 24px;
            transition:all 0.5s;
            -moz-transition:all 0.5s;
            -webkit-transition:all 0.5s;
            -o-transition:all 0.5s;
        }
        .mix_box .imgs_block .img .title2{
            bottom: 24px;
            display: none;
        }
        .mix_box .imgs_block .img:hover .img_mask{
            background: #000;
            background-color:rgba(0,0,0,0.5);
        }
        .mix_box .imgs_block .img:hover .title1{
            bottom: 82px;
            color: #FFF;
        }
        .mix_box .imgs_block .img:hover .title2{
            display: block;
        }

        .block_box{
            width: 100%;
            /*background: #f6f6f6;*/
        }
        .block_box .block_chain{
            margin: 0 auto;
            height: 500px;
            width: 1383px;
        }
        .block_box .block_chain img{
            margin-top: 100px;
        }
        .block_box .block_chain .title{
            position: relative;
            padding-top: 193px;
            padding-left: 243px;
        }
        .block_box .block_chain .title span{
            display: block;
        }
        .block_box .block_chain .title .title1{
            font-size: 36px;
            color: #555;
        }
        .block_box .block_chain .title i{
            position: absolute;
            display: block;
            width: 50px;
            height: 2px;
            background: #ff9211;
            margin-top: 20px;
        }
        .block_box .block_chain .title .title2{
            margin-top: 55px;
            font-size: 30px;
            color: #9e9e9e;
        }
        @media (max-width: 1900px) {
            .banner_box, .coins_box, .feature_box, .mix_box {
                height: 710px;
            }
            .ml124{
                margin-left: 103px;
            }

            .coins_box .coin_block {
                width: 1200px;
                height: 330px;
            }
            .coins_box .coin_block li {
                height: 330px;
            }
            .coins_box .coin_block .title {
                width: 172px;
                padding: 8px 0;
                font-size: 20px;
            }
            .coins_box .coin_block .text_box {
                margin-top: 27px;
                width: 169px;
                height: 258px;
            }
            .coins_box .coin_block .text_box .content {
                padding: 16px;
                line-height: 25px;
                font-size: 15px;
            }
            .coins_box .coin_block .center {
                width: 302px;
            }
            .coins_box .coin_block .center li {
                width: 100px;
            }
            .coins_box .coin_block .center .left {
                width: 65px;
                height: 19px;
                margin-top: 155px;
                margin-left: 35px;
            }
            .coins_box .coin_block .center li p {
                font-size: 50px;
                padding: 40px 8px;
            }
            .coins_box .coin_block .center .right {
                width: 65px;
                height: 19px;
                margin-top: 155px;
            }

            .feature_box .feature_block ul {
                width: 1200px;
            }
            .feature_box .feature_block li {
                width: 290px;
                height: 226px;
            }
            .feature_box .feature_block li .num {
                left: 40px;
                top: -13px;
                font-size: 40px;
                width: 66px;
                height: 73px;
                line-height: 73px;
            }
            .feature_box .feature_block li p {
                font-size: 14px;
                padding: 86px 20px;
                line-height: 20px;
            }
            .ml20{
                margin-left: 13px;
            }
            .feature_box .feature_block li:hover .num{
                height: 86px;
            }
            .feature_box .feature_block li:hover p{
                padding: 0 20px;
                padding-top: 93px;
                padding-bottom: 86px;
            }

            .mix_box .imgs_block {
                width: 1200px;
                height: 327px;
            }
            .mix_box .imgs_block .img {
                width: 390px;
                height: 289px;
            }
            .mix_box .imgs_block .img .title1 {
                bottom: 15px;
            }
            .mix_box .imgs_block .img span {
                font-size: 11px;
                line-height: 37px;
            }
            .mix_box .imgs_block .img:hover .title1{
                bottom: 52px;
            }

            .block_box .block_chain {
                height: 454px;
                width: 1200px;
            }
            .block_box .block_chain img {
                margin-top: 90px;
            }
            .block_box .block_chain .title {
                padding-top: 175px;
                padding-left: 114px;
            }
            .block_box .block_chain .title .title1 {
                font-size: 32px;
            }
            .block_box .block_chain .title i {
                width: 45px;
                margin-top: 18px;
            }
            .block_box .block_chain .title .title2 {
                margin-top: 50px;
                font-size: 27px;
            }
            .footer {
                margin-top: 0;
            }
        }
    </style>
</head>
<body>
<c:set var="menu_index" value="3"/>
<%@include file="../common/header.jsp" %>
<!-- 大图开始 -->
<div class="banner_box">
    <div class="banner">
        <ul class="banner_text">
            <li class="title">创新区块链资产的纳斯达克</li>
            <li class="sub_title">—<span></span>权益板块=区块链+实体资产+普惠金融<span></span>—</li>
        </ul>
    </div>
</div>
<div class="coins_box">
    <div class="coins_title"><span>国家数字货币与私人数字货币</span></div>
    <div class="block_box">
        <ul class="coin_block">
            <li>
                <span class="title">国家数字货币</span>
                <div class="text_box">
                    <p class="content">
                        国家数字货币发行的
                        前提是国家授权，法
                        律授权的。基于联盟
                        链或者分布式总账系
                        统，法律授权将来也
                        会成为数字货币发行
                        的一种方式。
                    </p>
                </div>
            </li>
            <li class="ml124">
                <span class="title">算法货币</span>
                <div class="text_box">
                    <p class="content">
                        区块链上的通用私人
                        货币，它依靠算法来
                        建立信用，这个算法
                        公之于众，依靠这样
                        一套算法，让大家相
                        信这个货币不会被乱
                        发，这是用算法建立
                        的信用，例如比特币、
                        众创币等。
                    </p>
                </div>
            </li>
            <li>
                <ul class="center">
                    <li><i class="left"></i></li>
                    <li><p>区块链资产</p></li>
                    <li><i class="right"></i></li>
                </ul>
            </li>
            <li>
                <span class="title">众筹货币</span>
                <div class="text_box">
                    <p class="content">
                        众筹货币基于区块链
                        之上的通用协议，你
                        要运用它，你就要使
                        用它的货币。所以说
                        它的功能就是更好地
                        帮助你去运行这些协
                        议，例如以太坊。
                    </p>
                </div>
            </li>
            <li class="ml124">
                <span class="title">资产锚定货币</span>
                <div class="text_box">
                    <p class="content">
                        资产锚定的货币，大部分都运行在基于区块链的商业应用上，促成了区块链与实体经济的紧密结合，例如燊活积分和堕落神探权益。
                    </p>
                </div>
            </li>
        </ul>
    </div>
</div>
<div class="feature_box">
    <div class="title_block">
        <ul>
            <li><span>资产锚定货币（权益）的特点</span></li>
            <li>每月两大子板块的币种在各自板块按单独交易额排名</li>
        </ul>
    </div>
    <div class="feature_block">
        <ul>
            <li>
                <div class="num">01</div>
                <p>
                    该资产具有合法企业主体
                </p>
            </li>
            <li class="ml20">
                <div class="num">02</div>
                <p>
                    在区块链上发行，自主交易、自主结算
                </p>
            </li>
            <li class="ml20">
                <div class="num">03</div>
                <p>
                    具有合理的商业模式或者实体价值
                    支撑，该区块链资产可以按照一定方式
                    对应实体资产或权益
                </p>
            </li>
            <li class="ml20">
                <div class="num">04</div>
                <p>
                    降低了实体投资的门槛，且可以实时交
                    易退出
                </p>
            </li>
        </ul>
    </div>
</div>
<div class="mix_box">
    <ul class="title_block">
        <li>比特家权益板块积极推动区块链</li>
        <li>技术与实体资产的融合</li>
        <li>
            <div class="btns">
                <span>电影</span>
                <span class="ml10">游戏</span>
                <span class="ml10">智能科技产品</span>
                <span class="ml10">企业项目</span>
            </div>
        </li>
    </ul>
    <div class="imgs_block">
        <div class="img img1 fl">
            <div class="img_mask">
                <span class="title1">堕落神探</span>
                <span class="title2">电影</span>
            </div>
        </div>
        <div class="img img2 ml20 fl">
            <div class="img_mask">
                <span class="title1">比特风云</span>
                <span class="title2">电影</span>
            </div>
        </div>
        <div class="img img3 ml20 fl">
            <div class="img_mask">
                <span class="title1">燊活积分</span>
                <span class="title2">企业项目</span>
            </div>
        </div>
    </div>
</div>
<div class="block_box">
    <div class="block_chain">
        <img class="fl" src="${resources}/static/images/equality/04.jpg">
        <div class="fl title">
            <span class="title1">区块链资产2.0</span>
            <i></i>
            <span class="title2">比特家带领数字货币进入价值投资新时代</span>
        </div>
    </div>
</div>
<%@include file="../common/footer_edg.jsp"%>
</body>
</html>