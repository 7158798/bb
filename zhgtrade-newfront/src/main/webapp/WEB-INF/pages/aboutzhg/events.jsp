<!-- 首页 author:yujie 2016-05-24 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%--<base href="<%=basePath%>">--%>
    <title>${fns:getProperty('site_title')}</title>
    <meta charset="utf-8">
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit'/>
    <meta http-equiv="X-UA-COMPATIBLE" content="IE=edge,chrome=1"/>
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/aboutzhg/events.css">
</head>
<body>

<c:set var="menu_index" value="2"/>
<c:set var="header_style" value="header_about"/>
<%@include file="../common/header.jsp" %>

<div class="banner">
    <div class="carousel">
        <div class="item" style="background-image: url('${resources}/static/images/aboutzhg/events/banner3.png');background-position: 50% 50%;";>

        </div>
    </div>
</div>

<div class=" events_wrapper">
    <div class="content">
        <div class="top">
            <div class="fl left_line"><span></span></div>
            <div class="fl left_block"><span></span></div>
            <div class="fl center"><span>大事记</span></div>
            <div class="fl right_block"><span></span></div>
            <div class="fl right_line"><span></span></div>
        </div>
        <div class="more">
            <a>MORE<span></span>＞</a>
        </div>
        <div class="item double start" style="margin-top: 37px;">
            <div class="text" style="max-width: 532px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_39.png" width="180" height="180"  >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">June, 2016<br/>2016/6</p>
                    <p class="cb">众股交易平台全新版亮相，对关键技术进行性能优化，保障了用户交易的流畅性和安全性。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 736px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_38.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">May, 2015<br/>2016/5</p>
                    <p class="cb">深圳前海招股金融服务有限公司获得天使轮融资1500万，同时正式将控股深圳市招股科技有限公司。招股科技将集中精力负责区块链技术研究。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 765px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_37.png" width="180" height="180" 
                     >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">April, 2015<br/>2016/4</p>
                    <p class="cb">招股金服领投院线大电影《比特风云》正式在众创园开启天使轮融资，此轮融资以众筹观影权+比特兑数字资产模式开展，一天之内完美完成280万的众筹金额。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 485px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_36.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">March, 2016<br/>2016/3</p>
                    <p class="cb">招股科技两项软件著作权申请成功。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 733px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_35.png" width="180" height="180" 
                     >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">January, 2015<br/>2016/1</p>
                    <p class="cb">众创园新模式首次开始为中小企业提供融资服务，燊活馆的燊活积分项目1周之内完成500万融资，再次证明众创园众筹+交易新模式的市场影响力。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 610px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_34.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">December, 2015<br/>2015/12</p>
                    <p class="cb">众创园新模式首次切入影视领域，《堕落神探》电影众筹1天之内完成20万众筹额度。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 596px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_33.png" width="180" height="180" 
                     >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">15th December, 2015<br/>2015/12/15</p>
                    <p class="cb">招股金服投资拍摄《堕落神探》电影，并参加发布会。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 624px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_31.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">15th November, 2015<br/>2015/11/15</p>
                    <p class="cb">众创园改版上线，新平台将众筹业务划分为币创投，回报众筹和企业众筹三大版块。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 659px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_32.png" width="180" height="180" 
                     >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">16th November, 2015<br/>2015/11/16</p>
                    <p class="cb">中国科技第一站---高交会，招股科技受邀在清华大学展会参展。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 736px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_29.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">17th November, 2015<br/>2015/11/17</p>
                    <p class="cb">招股科技晋级“创启未来”国际青年科技创业大赛：深圳赛区总决赛第四名。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 708px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_30.png" width="180" height="180" 
                     >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">19th November, 2015<br/>2015/10/19</p>
                    <p class="cb">全国“双创周”参展深圳展区活动，招股科技受邀在清华大学展会参展。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 666px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_08.png" width="180" height="180"
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">12th October, 2015<br/>2015/10/12</p>
                    <p class="cb">深圳前海招股金融服务有限公司成立，公司定位于区块链金融和互联网金融服务。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 659px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_26.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">18th September, 2015<br/>2015/9/18</p>
                    <p class="cb">2015年9-10月，招股科技应邀参加南山区创业之星大赛，从2万家企业中杀进50强，晋级总决赛。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 645px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_21.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">15th August, 2015<br/>2015/8/15</p>
                    <p class="cb">招股科技入围深圳南山创业之星大赛-国际大学生联赛，成为币圈首支入选团队。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 683px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_17.png" width="180" height="180" 
                     >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">18th July, 2015<br/>2015/7/18</p>
                    <p class="cb">招股科技搬迁到全新办公楼，地址位于风景秀丽的大学城外世外桃源创意园，塑造了良好的企业形象，招股科技全新起航。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 638px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_15.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">8th July, 2015<br/>2015/7/8</p>
                    <p class="cb">招股科技喜获清华大学景芝创业大赛一等奖，得到了清华大学的高度重视、培养、支持。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 740px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_14.png" width="180" height="180" 
                     >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">29th June, 2015<br/>2015/6/29</p>
                    <p class="cb">招股科技接受币圈知名媒体【比特帮】头条专访，招股科技公司展示了公司的自主品牌产品，并透露了众创币项目的规划，双方达成深度战略合作。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 736px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_11.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">18th June, 2015<br/>2015/6/18</p>
                    <p class="cb">招股科技第二款智能新品：Z-Keyset蓝牙键盘，兼容安卓/IOS系统，极具性价比。Z-Keyset蓝牙键盘在众创币众筹活动中赠送出约100台，反响良好。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 764px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_10.png" width="180" height="180" 
                     >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">12th June, 2015<br/>2015/6/12</p>
                    <p class="cb">招股科技公司申请了“招股科技”四类【商标】，成为正式具有法律效力的品牌。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 624px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_40.png" width="180" height="180"
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">1st June, 2015<br/>2015/6/1</p>
                    <p class="cb">众创园全新上线，平台定位于流动性数字资产众筹，对接交易平台，解决了众筹的提前退出问题。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 659px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_07.png" width="180" height="180" 
                     >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">30th May, 2015<br/>2015/5/30</p>
                    <p class="cb">Z-Watch赞助清华大学深圳研究生院校园歌手大赛，招股智能产品以及旗下数字货币招股币首次登陆高校。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 677px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_06.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">24th May, 2015<br/>2015/5/24</p>
                    <p class="cb">招股科技受邀参加深圳市龙岗国际青创节，获得广东卫视独家采访，力推招股币和Z-Watch 1S ，并现场销售100余台手表。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 686px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_05.png" width="180" height="180" 
                     >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">8th May, 2015<br/>2015/5/8</p>
                    <p class="cb">招股科技公司官网 www.zhaogukeji.com 强势上线研发和生产智能硬件产品，定位于提供全面智能化生活的解决方案。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="width: 778px;">
                <img class="fr" src="${resources}/static/images/aboutzhg/events/events_04.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" >
                <div class="fr text_box" style="">
                    <p class="time">26th April, 2015<br/>2015/4/26</p>
                    <p class="cb">招股科技受邀参加清华大学104周年校庆深圳校友会庆祝大会，CEO鄢傲受到清华校长邱勇接见，鄢傲汇报了公司规划和业绩，并展示Z-Watch。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item double tip" style="margin-top: -40px;">
            <div class="text" style="width: 743px;">
                <img class="fl" src="${resources}/static/images/aboutzhg/events/events_03.png" width="180" height="180" 
                     >
                <img class="right_mouth" src="${resources}/static/images/aboutzhg/events/right.png" >
                <div class="fl text_box" style="">
                    <p class="time">10th April, 2015<br/>2015/4/10</p>
                    <p class="cb">招股科技与智能硬件公司合作，组建团队、投入巨资，开发Z-Watch智能手表。</p>
                </div>
            </div>
            <img class="zhg" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip" style="margin-top: -40px;">
            <div class="text" style="height: 274px;">
                <img style="margin-top: 25px;" class="fr" src="${resources}/static/images/aboutzhg/events/events_01.png" width="180" height="180" 
                     >
                <img class="left_mouth" src="${resources}/static/images/aboutzhg/events/left.png" style="top: 104px;" >
                <div class="fr text_box" style="">
                    <p class="time">18th March, 2015<br/>2015/3/18</p>
                    <p class="cb">深圳市招股科技有限公司成立（简称：招股科技），公司定位于打造数字货币和科技实体的开放生态链,
                        是一家数字货币生态链服务与智能硬件相结合的高科技公司。公司采用合伙人负责制，目前有技术、营销、管理和财务等4个终身合伙人，
                        他们均来毕业于清华大学，并且具有多年的互联网金融、虚拟货币、物联网等方向的实践经验。
                    </p>
                </div>
            </div>
            <img class="zhg" style="margin-top: 22px;" src="${resources}/static/images/aboutzhg/events/zhg.png" width="20" height="20" 
                 >
        </div>
        <div class="item tip end_item" style="margin-top: 80px;">
            <div class="end" style="">
                <p style="margin-top: 16px;">成长历程</p>
            </div>
        </div>
    </div>
</div>
<%--<div class="loading">--%>
<%--<i class="loader db fl"></i>--%>
<%--<span class="db fl">正在加载...</span>--%>
<%--</div>--%>
<%--<div class="nomore"></div>--%>
<%@include file="../common/footer_edg.jsp"%>
</body>
</html>