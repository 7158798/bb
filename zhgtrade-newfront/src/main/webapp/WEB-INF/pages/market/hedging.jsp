<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp"%>
<div class="hedging">
    <div class="title b_l_blue pl10 f16">${type.fname}币对冲</div>
    <div class="content clear">
        <div class="c_left fl">
            <div class="name fl"><img src="/static/images/zgc.png" width="50" height="50" alt=""><span class="text">招股币(ZGC)</span></div>
            <div class="price fl c_blue">0.511</div>
            <div class="cb"></div>
            <div class="time">
                币对冲时间：2016/06/06 —— 2016/06/07
            </div>
            <div class="rest_time">
                <div class="item1">距离币对冲结束时间还有：</div>
                <div class="item2">
                    <span class="num db fl">0</span>
                    <span class="num db fl">0</span>
                    <span class="txt db fl">天</span>
                    <span class="num db fl">1</span>
                    <span class="num db fl">1</span>
                    <span class="txt db fl">时</span>
                    <span class="num db fl">1</span>
                    <span class="num db fl">1</span>
                    <span class="txt db fl">分</span>
                    <span class="num db fl">1</span>
                    <span class="num db fl">1</span>
                    <span class="txt db fl">秒</span>
                </div>
            </div>
            <div class="mt20">已参与人数：<span class="c_blue">523人</span></div>
            <div class="mt20">抵押招股币：<span class="c_blue">50万（ZGC为涨10万，为平5万，为跌35万）</span></div>
        </div>
        <div class="c_right fl">
            <div class="c_title mt15">我要对冲</div>
            <div class="c_term1 mt15">您目前可用ZGC: <span class="c_blue">0</span> <a href="javascript:void(0)" class="c_blue">去充值</a></div>
            <div class="c_term2 clear">
                <a href="javascript:void(0)" class="cur">猜涨</a>
                <a href="javascript:void(0)">猜平</a>
                <a href="javascript:void(0)">猜跌</a>
            </div>
            <input type="text" class="db" placeholder="对冲招股币的数量"/>
            <div class="information">系统将会收取：<span class="c_blue">0.5ZGC</span></div>
            <input type="password" class="db" placeholder="交易密码"/>
            <a class="submit" href="javascript:void(0)">我要对冲</a>
            <a href="javascript:void(0)" class="look_log c_blue">查看我的对冲记录</a>
        </div>
    </div>
    <%--币对冲记录开始--%>
    <div class="title b_l_blue pl10 f16">${type.fname}币对冲记录</div>
    <div class="content log_content">
        <div class="log_title">抵押币：<span class="c_blue">50万ZGC</span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;参与人数：<span class="c_blue">523人</span></div>
        <div class="table">
            <p class="f14">
                <span>用户名</span>
                <span>猜涨</span>
                <span>看平</span>
                <span>猜跌</span>
                <span>时间</span>
            </p>
            <p>
                <span>182****9071</span>
                <span>-</span>
                <span>-</span>
                <span>1000ZGC</span>
                <span>2016/07/13 11:11:11</span>
            </p>
            <p>
                <span>182****9071</span>
                <span>-</span>
                <span>-</span>
                <span>1000ZGC</span>
                <span>2016/07/13 11:11:11</span>
            </p>
            <p>
                <span>182****9071</span>
                <span>-</span>
                <span>-</span>
                <span>1000ZGC</span>
                <span>2016/07/13 11:11:11</span>
            </p>
        </div>
    </div>
</div>