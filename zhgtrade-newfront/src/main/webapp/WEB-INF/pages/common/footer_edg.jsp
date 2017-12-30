<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<!-- 底部开始 -->--%>
<div class="footer" id="footer">
    <div class="information clear">
        <div class="help fl">
            <ul>
                <li class="fir">
                    <a href="javascript:void(0)">关于我们</a>
                </li>
                <li class="">
                    <a href="/about/events.html">大事记</a>
                </li>
                <li class="">
                    <a href="/about/managerteam.html">管理团队</a>
                </li>
                <li>
                    <a href="/about/newsmedia.html">媒体报道</a>
                </li>
                <li>
                    <a href="/about/contact.html">联系我们</a>
                </li>
            </ul>
            <ul>
                <li class="fir">
                    <a href="javascript:void(0)">新手帮助</a>
                </li>
                <li class="">
                    <a href="${requestScope.constant['footerArticle1']}">充值提现</a>
                </li>
                <li class="">
                    <a href="${requestScope.constant['footerArticle2']}">充币提币</a>
                </li>
                <li>
                    <a href="${requestScope.constant['footerArticle3']}">交易规则</a>
                </li>
                <li>
                    <a href="${requestScope.constant['footerArticle4']}">用户协议</a>
                </li>
            </ul>
            <ul class="">
                <li class="fir">
                    <a href="javascript:void(0)">商务合作</a>
                </li>
                <li class="">
                    <a href="${requestScope.constant['footerArticle5']}">上币申请</a>
                </li>
                <li class="">
                    <a href="${requestScope.constant['footerArticle6']}">众筹申请</a>
                </li>
                <li>
                    <a href="${requestScope.constant['footerArticle7']}" target="_blank">营销合作</a>
                </li>
                <li>
                    <a href="${requestScope.constant['footerArticle8']}">英才招募</a>
                </li>

            </ul>
            <ul class="last">
                <li class="fir">
                    <a href="javascript:void(0)">联系我们</a>
                </li>
                <li class="">
                    <a href="javascript:void(0)">客服QQ：${requestScope.constant['serviceQQ']}</a>
                </li>
                <li class="">
                    <a href="javascript:void(0)">客服电话：${requestScope.constant['telephone']}</a>
                </li>
                <li>
                    <a href="javascript:void(0)" target="_blank">工作时间：周一至周五 早9点半-晚6点</a>
                </li>
                <li>
                    <a href="javascript:void(0)">公司邮箱：${requestScope.constant['email']}</a>
                </li>

            </ul>
        </div>
        <div class="contact fr clear">
            <div class="group_wrapper fl">
                <img src="${resources}/static/images/index/weixin.jpg" alt="招股金服微信公众号 " width="105" height="105" titlte="招股微信" />
            </div>
        </div>
    </div>
    <div class="copyright">${requestScope.constant['webinfo'].fcopyRights }</div>
    <div class="change_to_mobile dn" id="change_to_mobile">
        <i class="iconfont db fl">&#xe646;</i>
        <span class="db fl">进入手机版</span>
    </div>
    <div class="tools dn" id="tools">
        <ul class="tool">
            <%--<li class="show_title" style="background-position:7px -249px;">
                <a class="db" target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${requestScope.constant['serviceQQ']}&site=qq&menu=yes">
                    <span class="info db">QQ客服</span>
                </a>
            </li>--%>
            <%--<li class="show_group" style="overflow:visible;background-position:-82px -249px;">
                <span class="info db">QQ群</span>
                <dl class="group_tab dn">
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=91cd9e73590d30369dc49889bfc2daf787f5bdc84cef4a361f07c0ea35d231a9"><dd>众股官方1群  436462447</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=6c66cddf9e0bc747ca77342dedcef3415636d2109d82c63b39aa424c59e7ed33"><dd>众股官方2群  469279756</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=c7d786d1715161143a09ee543417c3cca04ce69e6b166c07a972881e52c57a41"><dd>众股官方3群  436865112</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=ba04c26bcb3c01436fce306704fbcc2ea9a7775f4d30217855babff5e54626ce"><dd>众股官方4群  251987043</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=74a7ebc32d17b05bb3c4436ee90782619b11a79f88c9d2b69bcf1b7c561212c0"><dd>众股官方5群  436873356</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=20ecbb2876b4ee83ceb67bc58a3b47c66a85d02e6f966df949316a4cde50b720"><dd>众股官方6群  261908190</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=ae30253f1683c5fc8e42dbd9882670a2d27429458ddb6d2d0eefb2d8a0cc2030"><dd>众股官方7群  433100822</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=a17d80156dad0a01c7f4ef5ae96d5c2d6e53a5b4243cabb5671b15f0a2be505f"><dd>众股官方8群  416291334</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=7714c3804b5fdd7c444306ccda6a819063fce7972515a4fd0232d1bb8048f3e6"><dd>众股官方9群  472747965</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=ccd8a6a5c2ee37cefed3bc38c30d823f167dc2e22fb27431e88da6a672bfb25d"><dd>众股官方10群  512077889</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=8d3c39c84ea5bb4e7ac92f5c01afab89b6397f57d3b40dad63dabf149ee4ec63"><dd>众股官方11群  293932346</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=ccd2e808af4772ab72c65f2e9f02a08849a817a73ec56e09a30ba9f9a3f9757a"><dd>众股官方12群  436804857</dd></a>
                   <a class="db" target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=2b0f85d3abded60fdbbee5054d1002dd64bdb72e944c246e95e5236a1210ad44"><dd>众股官方13群  433241024</dd></a>
                </dl>
            </li>--%>
            <li class="weixin" style="background-position: -82px -203px">
                aa
                <%--<img src="${resources}/static/images/index/weixin.jpg" class="dn">--%>
            </li>
            <%--<li class="slidebox" style="background-position:7px -295px;">
                <a href="tel:${requestScope.constant['telephone']}"><span class="db phone">${requestScope.constant['telephone']}</span></a>
            </li>--%>
           <%-- <li class="show_title <c:if test="${!empty(sessionScope.login_user)}">send_message</c:if><c:if test="${empty(sessionScope.login_user)}">loginbeforesend</c:if>" style="background-position:7px -343px;">
                <span class="info db">我要反馈</span>
            </li>--%>
            <li id="totop" style="height:0px;background-position:7px -389px;"></li>
        </ul>
    </div>
</div>



<%--<!-- 底部结束 -->--%>
<%--<!-- 发起提问开始 -->--%>
<%--<!-- 发起提问弹出窗 -->--%>
<div id="send_message_box" class="float_box dn" style="top:200px;left:500px;">
    <div class="f_title pl10">
        <h3 class="db fl">我要提问</h3>
        <i class="iconfont c_gray db fr close">&#xe609;</i>
    </div>
    <div class="f_content">
        <form action="/account/submitQuestion">
            <p>
                <span class="db fl"><i class="c_red">*</i>问题类型：</span>
                <select class="db fl">
                    <option value="-1">---请选择问题类型---</option>
                </select>
            </p>
            <p>
                <span class="fir db fl"><i class="c_red">*</i>问题描述：</span>
                <textarea class="pl5" name="desc"></textarea>
            </p>
            <p>
                <span class="fir db fl"><i class="c_red">*</i>姓名：</span>
                <input type="text" name="name">
            </p>
            <p>
                <span class="fir db fl"><i class="c_red">*</i>电话：</span>
                <input type="text" name="telephone" onkeyup="value=value.replace(/[^\d]/g,'')">
            </p>
            <div>
                <span style="color: red;padding-left: 115px;" id="errorMsg">&nbsp;</span><br>
                <a onclick="submitQuestionOnPop()" class="questionButtonblue" href="javascript:void(0)">提交问题</a>
            </div>
        </form>
    </div>
</div>
<%--<!-- 发起提问结束-->--%>
<script src="${resources}/static/js/utils/popup_dialog.js"></script>
<script src="${resources}/static/js/jquery/jquery.cookie.js"></script>
<script src="${resources}/static/js/jquery/jquery.pjax.min.js"></script>
<script src="${resources}/static/js/utils/util.js"></script>
<script src="${resources}/static/js/form.js"></script>
<script src="${resources}/static/js/common.js"></script>
<script src="${resources}/static/js/footer.js"></script>
<script type="text/javascript">
//    (function(){var time = new Date().getTime()-loadTime;var args = ['time=' + time,'url=' + location.href, "app=emhndHJhZGU="];var img = new Image(1, 1);img.src = location.protocol + '//www.zhgtrade.com/load.gif?' + args.join("&");})()
</script>
<style>a[title="站长统计"]{display: none;}</style>
<script src="//s11.cnzz.com/z_stat.php?id=1257639174&web_id=1257639174" language="JavaScript"></script>
<div class="footer_safe">
    <a href="http://www.12377.cn/" target="_blank" class="icon fir"></a>
    <a target="_blank" href="http://webscan.360.cn/index/checkwebsite/url/www.zhgtrade.com" class="icon s360"></a>
    <a target="_blank" href="http://www.cyberpolice.cn/wfjb/" class="icon sec"></a>
    <!--可信网站图片LOGO安装开始-->
    <span style="display:inline-block;position:relative;width:auto;">
        <a href="https://ss.knet.cn/verifyseal.dll?sn=e17022244030066726oe9m000000&amp;ct=df&amp;a=1&amp;pa=0.02793734461092079" id="kx_verify" tabindex="-1" target="_blank" kx_type="图标式" style="display:inline-block;">
            <img src="//zhgtrade.oss-cn-qingdao.aliyuncs.com/img/knet/cnnic.png" style="border:none;" oncontextmenu="return false;" alt="可信网站">
        </a>
    </span>
    <!--可信网站图片LOGO安装结束-->
</div>