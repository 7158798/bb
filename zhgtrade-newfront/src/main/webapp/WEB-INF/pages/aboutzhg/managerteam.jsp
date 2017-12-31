<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta charset="utf-8">
    <meta content=always name=referrer>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <style type="text/css">
            body {
                background: rgb(250,250,250);
            }
            .banner .carousel {
                position: relative;
                height: 420px;
            }

            .banner .carousel .item {
                position: absolute;
                left: 0;
                right: 0;
                width: 100%;
                height: 100%;
                background-position: 50% 50%;
            }
            .profile {
                font-size: 18px;
                line-height: 28px;
                color: #646464;
            }
            .group {
                color: #333333;
                height: 20px;
                margin: 62px auto;
                max-width: 1200px;
                display: -webkit-flex;
            }
            .group h1 {
                float: left;
                font-size: 24px;
                margin: -10px 40px 0;
            }
            .group div {
                float: left;
            }
            .group .line {
                background: #bbbbbb;
                height: 1px;
                margin-top: 3px;
                width: 450px;
                -webkit-flex: 1;
            }
            @media (min-width: 1200px) {
                .group .line {
                    width: 450px;
                }
            }
            .group .lt {
                width: 60px;
                height: 8px;
                background: #e7c049;
            }
            .group .rt {
                width: 60px;
                height: 8px;
                background: #afccd4;
            }
            .tit h1 {
                font-size: 26px;
                color: #41adde;
                padding: 16px 0 10px;
            }
            .tit span {
                padding: 0 20px;
            }
            .center {
                text-align: center;
            }
            .desc {
                font-size: 16px;
                width: 900px;
                margin: 10px auto 0;
                background: #e8e8e8;
                padding: 32px 0;
                border-radius: 6px;
                color: #646464;
            }
            .carousel {
                height: 493px;
            }
            .carousel-control.left,.carousel-control.right {
                background: none;
            }
            .carousel-control.left > span {
                left: 15%;
            }
            .carousel-control.right > span {
                right: 15%;
            }
            .carousel-control > span {
                color: #41adde;
                top: 28%;
                position: absolute;
                font-size: 35px;
                width: 20%;
                padding: 20px 0;
            }
            .carousel-indicators li {
                width: 16px;
                height: 5px;
                background-color: #e8e8e8;
                margin: 0 3px;
            }
            .carousel-indicators .active {
                width: 16px;
                height: 5px;
                background-color: #41adde;
                margin: 0 3px;
            }
            .carousel-control > span:hover {
                background-color: #41adde;
                color: #FFF;
            }
            .h1, .h2, .h3, .h4, .h5, .h6, h1, h2, h3, h4, h5, h6 {
                line-height: inherit;
            }
            img {
                vertical-align: inherit;
            }
            * {
                box-sizing: content-box;
                -webkit-box-sizing: content-box;
            }
            a:hover, a:focus {
                text-decoration: none;
            }

        </style>
</head>
<body>

<c:set var="menu_index" value="3"/>
<c:set var="header_style" value="header_about"/>
<%@include file="../common/header.jsp" %>

<div class="banner">
    <div class="carousel">
        <div class="item" style="background-image: url('${resources}/static/images/aboutzhg/managerteam/banner.jpg');">

        </div>
    </div>
</div>

<div class="profile">
    <div class="group">
        <div class="line"></div>
        <div class="lt"></div>
        <h1>集团团队</h1>
        <div class="rt"></div>
        <div class="line"></div>
    </div>
</div>

    <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
        <!-- Indicators -->
        <ol class="carousel-indicators">
            <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
            <li data-target="#carousel-example-generic" data-slide-to="1"></li>
            <li data-target="#carousel-example-generic" data-slide-to="2"></li>
            <li data-target="#carousel-example-generic" data-slide-to="3"></li>
            <li data-target="#carousel-example-generic" data-slide-to="4"></li>
            <li data-target="#carousel-example-generic" data-slide-to="5"></li>
            <%--<li data-target="#carousel-example-generic" data-slide-to="6"></li>--%>
        </ol>

        <!-- Wrapper for slides -->
        <div class="carousel-inner" role="listbox">
            <div class="item active">
                <div class="profile">
                    <div class="center">
                        <p class="pic"><img src="${resources}/static/images/aboutzhg/managerteam/01.png" alt="鄢傲"></p>
                        <div class="tit">
                            <h1>
                                鄢傲<span>比特家合伙人</span>CEO
                            </h1>
                        </div>
                        <p>籍贯：湖北天门</p>
                        <p>毕业院校：清华大学，研究生学历</p>
                        <div class="desc">
                            <p>在物联网领域累计发表多篇国际论文；曾工作于Hitachi（China）和招商银行总行，在物</p>
                            <p>联网和数字货币领域具有多年的实践经验。主要负责集团的整体运营</p>
                            <p>管理、商务合作，并负责众筹项目甄选。</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="item">
                <div class="profile">
                    <div class="center">
                        <p class="pic"><img src="${resources}/static/images/aboutzhg/managerteam/02.png" alt="程超"></p>
                        <div class="tit">
                            <h1>
                                程超<span>比特家合伙人</span>CTO
                            </h1>
                        </div>
                        <p>籍贯：河南新县</p>
                        <p>毕业院校：清华大学，研究生学历</p>
                        <div class="desc">
                            <p>在混合网络及视频加密领域累计发表多篇国际论文，曾工作于网易游戏，并参与多项工程项目开发，具</p>
                            <p>备丰富的研发经验。主要负责集团比特家、众创园等业务线的研发和业</p>
                            <p>务合作，并主导招股Z-Watch等智能硬件软件系统开发。</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="item">
                <div class="profile">
                    <div class="center">
                        <p class="pic"><img src="${resources}/static/images/aboutzhg/managerteam/03.png" alt="向忠胜"></p>
                        <div class="tit">
                            <h1>
                                向忠胜<span>比特家合伙人</span>CMO
                            </h1>
                        </div>
                        <p>籍贯：江苏扬州</p>
                        <p>毕业院校：清华大学，研究生学历</p>
                        <div class="desc">
                            <p>在超大规模网络系统仿真领域累计发表了多篇国际论文，曾工作于Ericsson集团、意法半导体（中国）、</p>
                            <p>华为控股集团，在网络营销和数字货币领域具有多年的实践经验。目前主要</p>
                            <p>负责产品营销推广、新业务扩展、以及集团品牌维护。</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="item">
                <div class="profile">
                    <div class="center">
                        <p class="pic"><img src="${resources}/static/images/aboutzhg/managerteam/04.png" alt="张国勇"></p>
                        <div class="tit">
                            <h1>
                                张国勇<span>比特家合伙人</span>CFO
                            </h1>
                        </div>
                        <p>籍贯：河南信阳</p>
                        <p>毕业院校：清华大学，研究生学历</p>
                        <div class="desc">
                            <p>在操作系统和系统级SoC设计领域累计发表了多篇国际论文；曾工作于联想集团和招商银行总行，</p>
                            <p>在数字货币领域具有多年的实践经验。主要负责集团财务管理以及</p>
                            <p>人力资源工作，并协助CEO统筹众筹项目甄选。</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="item">
                <div class="profile">
                    <div class="center">
                        <p class="pic"><img src="${resources}/static/images/aboutzhg/managerteam/05.png" alt="夏郑渠"></p>
                        <div class="tit">
                            <h1>
                                夏郑渠<span>比特家战略顾问</span>
                            </h1>
                        </div>
                        <p>&nbsp;</p>
                        <p>毕业院校：北京交通大学</p>
                        <div class="desc">
                            <p>曾就职于华为，日海通讯以及宇顺电子股份，目前担任前海汇潮投资管理合伙人以及招股</p>
                            <p>金服首席战略顾问，在信息和金融领域拥有二十余年实战经验。</p>
                            <p>&nbsp;</p>
                        </div>
                    </div>
                </div>
            </div>
            <%--<div class="item">--%>
                <%--<div class="profile">--%>
                    <%--<div class="center">--%>
                        <%--<p class="pic"><img src="${resources}/static/images/aboutzhg/managerteam/06.png" alt="宋卫强"></p>--%>
                        <%--<div class="tit">--%>
                            <%--<h1>--%>
                                <%--宋卫强<span>比特家董事</span>--%>
                            <%--</h1>--%>
                        <%--</div>--%>
                        <%--<p>&nbsp;</p>--%>
                        <%--<p>毕业院校：西安电子科技大学</p>--%>
                        <%--<div class="desc">--%>
                            <%--<p>有多年金融行业从业经验，在投行，银行等部门有过任职经历，工作内容包括外汇交</p>--%>
                            <%--<p>易，银行信托业务，资产处置等。</p>--%>
                            <%--<p>&nbsp;</p>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
            <div class="item">
                <div class="profile">
                    <div class="center">
                        <p class="pic"><img src="${resources}/static/images/aboutzhg/managerteam/07.png" alt="叶晓东"></p>
                        <div class="tit">
                            <h1>
                                叶晓东<span>法务顾问</span>
                            </h1>
                        </div>
                        <p>&nbsp;</p>
                        <p>毕业院校：江西大学(现南昌大学)法律系</p>
                        <div class="desc">
                            <p>曾任中国平安保险股份总办法律顾问，广东益商律师事务所合伙人，现任北京市大成律师</p>
                            <p>事务所分所高级合伙人。专业领域包括金融、保险、房地</p>
                            <p>产、集团法律事务，具有数十年从业经验。</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Controls -->
        <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
            <span class="glyphicon glyphicon-menu-left" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
            <span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>

<%@include file="../common/footer_edg.jsp"%>
<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js" rel="stylesheet"></script>
</body>
</html>
