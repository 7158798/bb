<%--
  Created by IntelliJ IDEA.
  User: sunpeng
  Date: 2016/5/20 0020
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%--<base href="<%=basePath%>">--%>
    <title>${fns:getProperty('site_title')}</title>
    <meta charset="utf-8">
    <meta name='renderer' content='webkit'/>
    <meta http-equiv="X-UA-COMPATIBLE" content="IE=edge,chrome=1"/>
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <%--<link href="${resources}/static/css/bootstrap.css" rel="stylesheet">--%>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
        <style>
            .m5 {  margin-left: 30px;  margin-right: 30px;  }
            body {  background: #fafafa;  }
            .banner .carousel {  position: relative;  height: 420px;  }
            .banner .carousel .item {  position: absolute;  left: 0;  right: 0;  width: 100%;  height: 100%;  z-index: 1;  background-position: 50% 50%;  }
            .center {  width: 1200px;  margin: auto;  }
            .center .top {  padding-top: 62px;  padding-bottom: 62px;  }
            #lefthorizontal {  background-color: #bbb;  float: left;  width: 450px;  margin-top: 4px  }
            #righthorizontal {  background-color: #bbb;  float: right;  width: 450px;  margin-top: 4px  }
            .center .top .title {  display: block;  float: left;  text-align: center;  font: 24px "Microsoft YaHei", Helvetica, Arial, sans-serif;  color: #323232;  width: 180px;  line-height: 7px;  }
            .center .top .leftRect {  float: left;  width: 60px;  height: 8px;  background-color: #E7C049;  }
            .center .top .rightRect {  float: right;  width: 60px;  height: 8px;  background-color: #aeccd4;  }
            .center .middle .row {  width: 100%;  height: 300px;  clear: both  }
            .center .middle .news {  float: left;  width: 380px;  position: relative;  }
            .center .middle a{  display: block;  overflow: hidden;  }
            .center .middle a:hover{  box-shadow: 0 0 10px gray;  }
            .center .middle .news img {  width: 100%;  height: 220px;  transition: all 1s ease;  }
            .center .middle a:hover img{  transform: scale(1.3);  -webkit-transform: scale(1.3);  -webkit-transform: all 1s ease 0s;  }
            .center .middle .news .project_info{  width: 100%;  height: 100%;  background: rgba(0,0,0,0);  position: absolute;  left: 0;  top: 220px;  height:40px;  }
            .center .middle .news .project_info div{  position:absolute;  top:0px;left:0px;z-index:2; /*transition: all 0.4s ease-out 0s;*/  }
            .center .middle .news p {  height:40px;  line-height:40px;  text-align: center;  font-size: 14px;  color: black;  width:380px;  }
            .center .middle .news .project_info:after{  width: 0px;  height: 40px;  display:block;  position:absolute;top:0px;left:0px;  content: "";  transition: all 0.4s cubic-bezier(0.215, 0.61, 0.355, 1) 0s;  background-color: rgba(254, 116, 31, 0.9);  z-index:1;  }
            .center .middle .news a:hover p{  color: #feffff;  }
            .center .middle a:hover .project_info:after {  width: 380px;  }
        </style>
</head>
<body>

<c:set var="menu_index" value="4"/>
<c:set var="header_style" value="header_about"/>
<%@include file="../common/header.jsp" %>

<div class="banner">
    <div class="carousel">
        <div class="item" style="background-image: url('${resources}/static/images/aboutzhg/newsmedis/banner.jpg')" ;>

        </div>
    </div>
    <%--<img src="${resources}/static/images/aboutzhg/newsmedis/banner.jpg">--%>
</div>

<div class="center">
    <div class="top">
        <hr id="lefthorizontal">
        <div class="leftRect"></div>
        <span class="title">媒体报道</span>
        <hr id="righthorizontal">
        <div class="rightRect"></div>
    </div>

    <div class="middle">
        <div class="row">
            <div class="news">
                <a href="http://finance.china.com/fin/qy/201507/28/4772035.html?qq-pf-to=pcqq.c2c" target="_blank">
                    <img src="${resources}/static/images/aboutzhg/newsmedis/1.jpg" width="380" height="220">
                    <div class="project_info">
                        <div>
                            <p class="title">招股新创举，物联大事业</p>
                        </div>
                    </div>
                </a>
            </div>
            <div class="news m5">
                <a href="/guide/article.html?id=44" target="_blank">
                    <img src="${resources}/static/images/aboutzhg/newsmedis/2.jpg" width="380" height="220">
                    <div class="project_info">
                        <div>
                            <p>比特家科技微创新，Z-Watch闪耀新登场</p>
                        </div>
                    </div>
                </a>
            </div>
            <div class="news">
                <a href="http://hebei.sina.com.cn/lf/focus/2015-04-17/15496108.html" target="_blank">
                    <img src="${resources}/static/images/aboutzhg/newsmedis/3.jpg" width="380" height="220">
                    <div class="project_info">
                        <div>
                            <p>清华创业团队将推出招股Z-Watch智能手表</p>
                        </div>
                    </div>
                </a>
            </div>
        </div>


        <div class="row">
            <div class="news">
                <a href="http://hebei.news.163.com/15/0415/15/AN8KN8QA02790M8B.html" target="_blank">
                    <img src="${resources}/static/images/aboutzhg/newsmedis/4.jpg" width="380" height="220">
                    <div class="project_info">
                        <div>
                            <p>比特家交易所：互联网金融创新思维</p>
                        </div>
                    </div>
                </a>
            </div>
            <div class="news m5">
                <a href="http://www.1-en.com.cn/redian/17477.html" target="_blank">
                    <img src="${resources}/static/images/aboutzhg/newsmedis/5.jpg" width="380" height="220">
                    <div class="project_info">
                        <div>
                            <p>众创币团队：用专利捍卫数字货币与物联网相结合的商业价值</p>
                        </div>
                    </div>
                </a>
            </div>
            <div class="news">
                <a href="http://mt.sohu.com/20150629/n415842841.shtml" target="_blank">
                    <img src="${resources}/static/images/aboutzhg/newsmedis/6.jpg" width="380" height="220">
                    <div class="project_info">
                        <div>
                            <p>币圈再现劲旅 Bithome卧虎藏龙</p>
                        </div>
                    </div>
                </a>
            </div>
        </div>

        <div class="row">
            <div class="news">
                <a href="/guide/article.html?id=47" target="_blank">
                    <img src="${resources}/static/images/aboutzhg/newsmedis/7.jpg" width="380" height="220">
                    <div class="project_info">
                        <div>
                            <p>“Bithome”开辟互联网金融新领域</p>
                        </div>
                    </div>
                </a>
            </div>
            <div class="news m5">
                <a href="http://yueyu.cntv.cn/2015/10/16/ARTI1444958185295850.shtml" target="_blank">
                    <img src="${resources}/static/images/aboutzhg/newsmedis/8.jpg" width="380" height="220">
                    <div class="project_info">
                        <div>
                            <p>清华学子双肩挑，比特家科技显精彩</p>
                        </div>
                    </div>
                </a>
            </div>
            <div class="news">
                <a href="http://btckan.com/news/topic/18839?bkuserid=14260&bkfrom=appshare&bktarget=circle&from=singlemessage&isappinstalled=0"
                   target="_blank">
                    <img src="${resources}/static/images/aboutzhg/newsmedis/9.jpg" width="380" height="220">
                    <div class="project_info">
                        <div>
                            <p>天使轮1500万，比特家斩获国内区块链行业最大规模融资</p>
                        </div>
                    </div>

                </a>
            </div>

        </div>


    </div>
</div>

<%@include file="../common/footer_edg.jsp"%>
</body>
</html>