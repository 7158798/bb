<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style rel="stylesheet">#bit span:hover{background:#fff;}</style>
<div id="top_menubar" class="top_nav fr" style="position:relative;">
    <ul>
        <li <c:if test="${'1' == menu_index}">class="current"</c:if>>
            <a href="/index.html" title=""><span>首页</span></a>
        </li>
        <%--<li <c:if test="${'2' == menu_index}">class="current"</c:if>>--%>
            <%--<a href="/coin/index.html" title=""><span>交易中心</span></a>--%>
        <%--</li>--%>
        <%--<li <c:if test="${'3' == menu_index}">class="current"</c:if>><a href="/equity/index.html"--%>
        <%--title=""><span>权益交易</span></a></li>--%>
        <li <c:if test="${'4' == menu_index}">class="current"</c:if>><a href="/account/fund.html" title=""><span>财务中心</span></a>
        </li>
        <%--<li <c:if test="${'5' == menu_index}">class="current"</c:if>><a href="http://www.zcfunding.com"--%>
        <%--title="http://www.zcfunding.com" target="_blank"><span>众筹平台</span></a></li>--%>

        <li <c:if test="${'6' == menu_index}">class="current"</c:if>><a href="/guide/help.html"
                                                                        title=""><span>新闻中心</span></a></li>
        <style rel="stylesheet">
            #bit{position:relative;border:1px solid transparent;height:70px;z-index:2;}
            #bit span{color:#ff3030;}
            /*  #bit dl{box-shadow:0 0px 4px #d9d9d9;position: absolute;top: 69px;  right: -1px;  background: #fff;  width: 104px;  border: 1px solid #d9d9d9;  border-top: none;}
              #bit dl dt{height:30px;line-height:30px;font-size:12px;color:#ff3030;}
              #bit dl dt a{color:#ff3030;height:30px;display:block;width:100%;}
              #bit dl dt a:hover{background:#f1f1f1;}*/
            #consult_img{margin-top:10px;}
        </style>

        <%--<li class="ml45"><a href="http://news.zhgtrade.com" title="资讯平台"><img id="consult_img" src="${cdn}/static/images/index/consult.png" /></a></li>--%>
        <%--<li id="bit" <c:if test="${'9' == menu_index}">class="current"</c:if>>--%>
        <%--<a href="/activity/btc_actor.html" target="_blank"><span style="" class="fb">比特风云专题</span></a>--%>
        <%--&lt;%&ndash;<dl class="dn">--%>
        <%--<dt><a href="/activity/btc_actor" target="_blank">竞猜活动</a></dt>--%>
        <%--<dt><a href="/answer/btc_answer" target="_blank">答题活动</a></dt>--%>
        <%--</dl>&ndash;%&gt;--%>
        <%--</li>--%>
    </ul>
    <%--<img style="display:block;position:absolute;right:1px;top:1px" src="${resources}/static/images/huo-3.gif" width="30" height="21" />--%>
</div>