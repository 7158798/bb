<!-- 安全中心页面author:xxp 2016-04-24 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit' />
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/account.css"/>
    <link rel="stylesheet" href="${resources}/static/css/selector.css" />
    <style type="text/css">
        .ml72 {
            margin-left: 72px;
        }
        .sel_w120{width: 120px;}
        .sel_h30{height: 30px}
    </style>
</head>
<body>
<c:set var="menu_index" value="4"/>
<%@include file="../common/header.jsp" %>
<div class="center_page">
    <div class="account_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="/account/security.html" class="f12 c_blue">基本设置</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_gray">个人信息</a>
    </div>
    <c:set var="dt_index" value="2"/>
    <c:set var="dd_index" value="9"/>
    <%@ include file="../common/account_left.jsp" %>
    <div class="account_right fl">
        <div class="finance_wrapper">
            <div class="safe_center fill_right content_wrapper">
                <h1 class="ml40">个人信息</h1>
                <div class="content mt10 ml40" style="padding: 0px;">
                    <iframe class="dn" name="upload_image"></iframe>
                    <form id="upload_head_image" target="upload_image" action="/account/upload_head_img.html" method="post" enctype="multipart/form-data">
                        <p>
                            <span style="width: 80px; height: 80px;border: 1px solid #ddd; position: relative;margin-left: 110px;" class="db">
                                <c:choose>
                                    <c:when test="${!empty headImgUrl}">
                                        <img id="headImage" style="position: absolute;top: 0;left: 0;" width="80" height="80" src="${cdn}/${headImgUrl}">
                                    </c:when>
                                    <c:otherwise>
                                        <img id="headImage" style="position: absolute;top: 0;left: 0;" width="80" height="80" src="${cdn}/static/images/default_head.jpg">
                                    </c:otherwise>
                                </c:choose>
                                <input id="headImageBtn" name="head_img" onchange="uploadImg()" title="更换头像" style="position: absolute;top: 0;left: 0;width: 80px; height: 80px;opacity:0;filter:alpha(opacity=0);background:none;cursor: pointer;" accept=".gif,.jpg,.jpeg,.png" type="file">
                            </span>
                        </p>
                    </form>
                        <p>
                            <span class="fir db fl">ID：</span>
                            <span class="db fl">${fuser.fid}</span>
                        <p>
                            <span class="fir db fl">昵称：</span>
                            <span class="db fl"><input id="nickname" style="width: 110px;" class="fl" type="text" onkeyup="checkForm()"
                                                       name="nickName" require="false"
                                                       data-name="昵称" value="${fuser.fnickName}" data-val="${fuser.fnickName}" maxlength="20">
                                <span style="height: 29px; line-height: 29px;" class="info f12 db fl ml10">
                                    <i style="height: 29px;" class="iconfont"></i>
                                    <span class="c_gray" style="line-height: 29px;"></span>
                                </span>
                            </span>
                        </p>
                        <p>
                            <span class="fir db fl">邮箱：</span>
                            <span class="db fl">${fuser.femail}</span>
                        </p>
                        <p>
                            <span class="fir db fl">手机：</span>
                            <span class="db fl">${fuser.ftelephone}</span>
                        </p>
                        <p>
                            <span class="fir db fl">用户等级：</span>
                            <i class="iconfont db fl c_blue">&#xe615;</i>
                            <span class="db fl pl5">vip${level}</span>
                            <span class="db fl pl40">积分:</span>
                            <span class="db fl pl5">${fuser.fscore.fscore}</span>
                        </p>
                    <form id="personalForm" action="/account/modUserinfo.html" method="post">
                        <p>
                            <span class="fir db fl">联系地址：</span>
                            <span id="provinceSelector" class="selector fl">
                                <span class="selector_item">
                                    <span>请选择省</span>
                                    <input type="hidden" id="provinceAddr" <%--onclick="checkForm()"--%> name="province" value="" require="true" data-name="省份" data-ret="true">
                                </span>
                                <span class="selector_items dn">
                                    <a data-val="" data-id="">请选择省</a>
                                    <c:forEach items="${provinces}" var="v" varStatus="vs">
                                        <a data-val="${v.name}" data-id="${v.id}" class="<c:if test="${v.name == province}">cur</c:if>">${v.name}</a>
                                    </c:forEach>
                                </span>
                            </span>
                            <span id="citySelector" class="selector fl ml10">
                                <span class="selector_item">
                                    <span>请选择城市</span>
                                    <input type="hidden" name="city" <%--onclick="checkForm()"--%> require="true" data-name="城市" data-ret="true" id="cityAddress">
                                </span>
                                <span class="selector_items dn" id="cityList">
                                    <a data-val="">请选择城市</a>
                                </span>
                            </span>
                            <input type="text" class="db fl ml10" name="address" <%--onkeyup="checkForm()"--%> value="${address}"
                                   placeholder="行政区街道" require="true" data-name="联系地址"/>
                            <span style="height: 30px; line-height: 30px;" class="info f12 db fl ml10">
                                <i class="iconfont fl" style="height: 30px;line-height: 30px;display: inline-block;"></i>
                                <span class="c_gray fl" style="height: 30px;line-height: 30px;display: inline-block;"></span>
                            </span>
                        </p>
                        <p>
                            <span class="fir db fl">&nbsp;</span>
                            <a href="javascript:updateUserInfo(document.getElementById('personalForm'));"
                               class="db fl bg_blue c_white">确认修改</a>
                            <span style="height: 29px; line-height: 29px;" class="info f12 db fl ml10">
                                <i style="height: 29px;" class="iconfont"></i>
                                <span class="c_gray" style="line-height: 29px;"></span>
                            </span>
                        </p>
                    </form>
                </div>
                <div class="content tal info_content mt10 <c:choose><c:when test="${fuser.fhasRealValidate || fuser.fpostRealValidate}">ml72</c:when><c:otherwise>ml40</c:otherwise></c:choose>"
                     style="padding: 0px;">
                    <c:choose>
                        <c:when test="${fuser.fhasRealValidate}">
                            <div class="c_title c_blue f18 ">
                                <i class="iconfont">&#xe612;</i>
                                <span>已通过实名认证</span>
                            </div>
                        </c:when>
                        <c:when test="${fuser.fpostRealValidate}">
                            <div class="c_title c_orange f18 ">
                                <i class="iconfont">&#xe612;</i>
                                <span>已提交实名认证</span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="c_title c_red f18 ">
                                <i class="iconfont">&#xe601;</i>
                                <span>未实名认证</span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${fuser.fhasRealValidate || fuser.fpostRealValidate}">
                            <p>
                                <span>证件类型：</span>
                                <%--<span>${fuser.fidentityType_s}</span>--%>
                                <span>身份证</span>
                            </p>
                            <p class="mt20">
                                <span>真实姓名：</span>
                                <span>${fuser.frealName}</span>
                            </p>
                            <p>
                                <span>证件号码：</span>
                                <c:choose>
                                    <c:when test="${fn:length(fuser.fidentityNo) > 8}">
                                        <span>${fn:substring(fuser.fidentityNo, 0, 4)}******${fn:substring(fuser.fidentityNo, fn:length(fuser.fidentityNo) - 4, fn:length(fuser.fidentityNo))}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span>${fn:substring(fuser.fidentityNo, 0, 4)}************</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <p class="mt20">
                                <span class="db fl">证件认证：</span>
                                <c:choose>
                                    <c:when test="${0 == fuser.fIdentityStatus}">
                                        <span class="db fl">未上传</span>
                                        <span class="db fl" style="height: 30px;width: 30px;"></span>
                                        <a href="/account/uploadIdentifyPic.html" class="db fl bg_blue c_white">上传</a>
                                    </c:when>
                                    <c:when test="${1 == fuser.fIdentityStatus}">
                                        <span class="c_orange">待审核</span>&nbsp;<span class="c_gray">(1-2个工作日内)</span>
                                    </c:when>
                                    <c:when test="${2 == fuser.fIdentityStatus}">
                                        <span class="c_green">已通过</span>
                                    </c:when>
                                    <c:when test="${3 == fuser.fIdentityStatus}">
                                        <span class="db fl c_red">未通过</span>
                                        <span class="db fl" style="height: 30px;width: 30px;"></span>
                                        <a href="/account/uploadIdentifyPic.html" class="db fl bg_blue c_white">重新上传</a>
                                    </c:when>
                                </c:choose>
                            </p>
                        </c:when>
                        <c:otherwise>
                            <form id="authForm" action="/account/auth.html" method="post">
                                <p>
                                    <span class="db fl fir">证件类型：</span>
                                    <select name="authType" class="db fl">
                                        <option value="0">身份证</option>
                                        <%--<option value="2">护照</option>--%>
                                    </select>
                                    <span style="height: 29px; line-height: 29px; margin-bottom: 0px;"
                                          class="info f12 db fl ml10">
                                        <i class="iconfont"></i>
                                        <span class="c_gray"></span>
                                    </span>
                                </p>
                                <p class="mt20">
                                    <span class="db fl fir">真实姓名：</span>
                                    <input id="realName" type="text" name="realName" require="true" data-name="真实姓名"
                                           data-min="2" data-max="15" class="pl5 db fl"/>
                                    <span style="height: 29px; line-height: 29px; margin-bottom: 0px;"
                                          class="info f12 db fl ml10">
                                        <i class="iconfont"></i>
                                        <span class="c_gray"></span>
                                    </span>
                                </p>
                                    <%--<p class="mt20">
                                        <span class="db fl fir">确认真实姓名：</span>
                                        <input type="text" name="reRealName" require="true" data-name="确认真实姓名"
                                               class="pl5 db fl"/>
                                        <span style="height: 29px; line-height: 29px; margin-bottom: 0px;"
                                              class="info f12 db fl ml10">
                                            <i class="iconfont"></i>
                                            <span class="c_gray"></span>
                                        </span>
                                    </p>--%>
                                <p class="mt20">
                                    <span class="db fl fir">证件号码：</span>
                                    <input type="text" name="cardId" require="true" data-name="证件号码"
                                           data-min="10" class="pl5 db fl"/>
                                    <span style="height: 29px; line-height: 29px; margin-bottom: 0px;"
                                          class="info f12 db fl ml10">
                                        <i class="iconfont"></i>
                                        <span class="c_gray"></span>
                                    </span>
                                </p>
                                <%--<p>
                                    <span class="db fl fir">验证码：</span>
                                    <input name="code" type="text" class="pl5 db fl" require="true"
                                           data-name="验证码" data-min="4" data-max="6"/>
                                    <a id="authCodeBtn" onclick="sendSmsCaptcha(this);"
                                       href="javascript:void(0);"
                                       class="db fl send bg_gray ml10 border_gray"><span>发送验证码</span></a>
                                    <span style="height: 29px; line-height: 29px; margin-bottom: 0px;"
                                          class="info f12 db fl ml10">
                                        <i class="iconfont"></i>
                                        <span class="c_gray"></span>
                                    </span>
                                </p>--%>
                                <p>
                                    <a style="width: 56px;margin-left: 110px; cursor: pointer;"
                                       href="javascript:submitAuthInfo(document.getElementById('authForm'));"
                                       class="db fl bg_blue c_white">确认提交</a>
                                </p>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    <div class="cb"></div>
</div>
<script src="${resources}/static/js/jquery/selectordie.min.js"></script>
<script  src="${resources}/static/js/utils/selector.js"></script>
<script  src="${resources}/static/js/account/personal.js"></script>
<script  src="${resources}/static/js/region.js"></script>
<script>
    function showImage(path){
        if(path){
            $("#headImage").attr('src', path);
        }
    }
    function uploadImg(){
        var path = $('#headImageBtn').val();
        if(!path){
            return;
        }
        document.getElementById('upload_head_image').submit();
    }
    $("#provinceSelector").custom_selector({onChange : function (_input) {
        var $curItem = $('#provinceSelector .selector_items a.cur');
        var idStr = $curItem.data("id");
        var opts = '<a data-val="">请选择城市</a>';
        var $citySelector = $("#citySelector");

        if (idStr){
            var id = parseInt(idStr);
            var pKey = "r" + id;
            var cities = regionConf[pKey]["c"];
            for (var key in cities) {
                var val = cities[key];
                var name = val['n'];
                if ("${city}" === name) {
                    opts += '<a data-val="' + name + '" class="cur">' + name + '</a>';
                }else{
                    opts += '<a data-val="' + name + '">' + name + '</a>';
                }
            }
        }
        $citySelector.find(".selector_items").html(opts);
        $citySelector.custom_selector();
    }, initedTriggerChange : true});

    $(function(){
        $('#nickname').on('blur', function(){
            var $this = $(this);
            var name = $.trim($this.val());
            var orignalName = $this.data('val');
            if(!name){
                alert('请输入昵称！');
                return;
            }
            if(name == $.trim(orignalName)){
                return;
            }
            $.post('/account/updateNickname', {nickname: name}, function(data){
                if(200 == data.code){
                    alert('修改成功！');
                    $this.data('val', name);
                }else{
                    alert('修改昵称失败！' + data.message);
                }
            }, 'json');
        });
    });
</script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>
