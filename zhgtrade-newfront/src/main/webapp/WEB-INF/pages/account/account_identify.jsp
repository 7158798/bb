<!-- 安全中心页面author:yujie 2016-04-24 -->
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
        .identify_icon{
            background:url("${cdn}/static/images/account/identify.png") no-repeat;
        }
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
        <a href="javascript:void(0)" class="f12 c_gray">身份证上传</a>
    </div>
    <c:set var="dt_index" value="2"/>
    <c:set var="dd_index" value="9"/>
    <%@ include file="../common/account_left.jsp" %>
    <div class="account_right fl">
        <div class="finance_wrapper">
            <div class="safe_center fill_right content_wrapper">
                <h1 class="ml40">身份证上传</h1>
                <div class="content mt10 ml40" style="padding: 0px;">
                    <p>
                        <i class="db fl identify_icon" style="background-position-x: 0;background-position-y: -241px;width: 30px;height: 34px;"></i>
                        <span class="db fl ml15">请保证所传证件照对应示例红色框内<span class="c_blue">文字清晰可识别</span>！上传单个附近大小限制为<span class="c_blue">2M</span>，格式限定为<span class="c_blue">jpeg / jpg</span>。</span>
                    </p>
                    <div class="mt40">
                        <div class="ml70">
                            <div class="fl">
                                手持身份证正面<span class="c_blue" style="font-weight: bold;">(必选)：</span>
                                <div class="mt5" style="width: 202px;height: 152px;border: 1px dashed #08a3d7;position: relative;">
                                    <i class="db identify_icon" style="background-position-x: -124px;background-position-y: -243px;width: 38px;height: 37px;margin: 40px auto 0;"></i>
                                    <span class="db c_gray" style="margin-left: 60px;margin-top: 15px;">选择图片上传</span>
                                    <form id="fIdentityPathForm" target="upload_image" action="/account/upload_auth_img.html" method="post" enctype="multipart/form-data">
                                        <input type="hidden" name="dom_id" value="fIdentityPathForm">
                                        <input type="hidden" name="val_dom_id" value="fIdentityPath">
                                        <input name="img" onchange="uploadImage(this)" style="position: absolute;top: 0;left: 0;width: 195px; height: 150px;opacity:0;filter:alpha(opacity=0);background:none;cursor: pointer;z-index: 5;" accept=".jpg,.jpeg" type="file">
                                    </form>
                                </div>
                            </div>
                            <i class="db fl identify_icon" style="background-position-x: -58px;background-position-y: -246px;width: 40px;height: 30px;margin: 80px 80px;"></i>
                            <div class="fl" style="width: 195px;height: 115px;border: 1px solid #969696;margin: 36px 0;">
                                <i class="db identify_icon" style="background-position-x: 0;background-position-y: 0;width: 167px;height: 106px;margin: 5px 14px;"></i>
                            </div>
                        </div>
                    </div>
                    <div class="cb"></div>
                    <div class="mt40">
                        <div class="ml70">
                            <div class="fl">
                                手持身份证反面<span class="c_blue" style="font-weight: bold;">(必选)：</span>
                                <div class="mt5" style="width: 202px;height: 152px;border: 1px dashed #08a3d7;position: relative;">
                                    <i class="db identify_icon" style="background-position-x: -124px;background-position-y: -243px;width: 38px;height: 37px;margin: 40px auto 0;"></i>
                                    <span class="db c_gray" style="margin-left: 60px;margin-top: 15px;">选择图片上传</span>
                                    <form id="fIdentityPath2Form" target="upload_image" action="/account/upload_auth_img.html" method="post" enctype="multipart/form-data">
                                        <input type="hidden" name="val_dom_id" value="fIdentityPath2">
                                        <input type="hidden" name="dom_id" value="fIdentityPath2Form">
                                        <input name="img" onchange="uploadImage(this)" style="position: absolute;top: 0;left: 0;width: 195px; height: 150px;opacity:0;filter:alpha(opacity=0);background:none;cursor: pointer;z-index: 5;" accept=".jpg,.jpeg" type="file">
                                    </form>
                                </div>
                            </div>
                            <i class="db fl identify_icon" style="background-position-x: -58px;background-position-y: -246px;width: 40px;height: 30px;margin: 80px 80px;"></i>
                            <div class="fl" style="width: 195px;height: 115px;border: 1px solid #969696;margin: 36px 0;">
                                <i class="db identify_icon" style="background-position-x: 0;background-position-y: -113px;width: 167px;height: 110px;margin: 3px 14px;"></i>
                            </div>
                        </div>
                    </div>
                    <div class="cb"></div>
                    <div class="mt40">
                        <form id="identifyForm" action="/account/uploadIdentifyPic.html" method="post">
                            <input type="hidden" id="fIdentityPath" name="fIdentityPath" value="">
                            <input type="hidden" id="fIdentityPath2" name="fIdentityPath2" value="">
                        </form>
                        <a href="javascript:doUploadImage();" class="db fl bg_blue c_white" style="border-radius: 3px;text-align: center;line-height: 50px;width: 170px;margin-left: 290px;font-size: 18px;">保存</a>
                    </div>
                    <iframe class="dn" name="upload_image"></iframe>
                    <%--<form id="upload_head_image" target="upload_image" action="/account/upload_head_img.html" method="post" enctype="multipart/form-data">
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
                    </form>--%>
                </div>
            </div>
        </div>
    </div>
    <div class="cb"></div>
</div>
<script>
    function uploadImage(dom){
        var $this = $(dom);
        var path = $this.val();
        if (path) {
            path = path.replace(/\s/g, '').toLowerCase();
        }
        if(!/\.jpeg$/.test(path) && !/\.jpg$/.test(path)){
            alert('请上传jpeg/jpg图片');
            return;
        }
        if(!path){
            return;
        }
        $this.parent().submit();
    }
    function showImage2(url, uri, dom_id){
        var $form = $('#' + dom_id);
        var val_dom = $form.find("input[name='val_dom_id']").val();
        $('#' + val_dom).val(uri);
        $form.find('img').remove();
        var img_dom = "<img id='headImage' style='position: absolute;top: 0;left: 0;margin: 5px;' width='190' src='" + url + "?x-oss-process=image/resize,h_140,w_190,m_pad'>";
        $form.append(img_dom);
    }
    function doUploadImage(){
        var $form = $('#identifyForm');
        if(!$form.find("input[name='fIdentityPath']").val()){
            alert('请重新上传正面证件照');
            return;
        }
        if(!$form.find("input[name='fIdentityPath2']").val()){
            alert('请重新上传反面证件照');
            return;
        }
        $.post($form.attr('action'), $form.serialize()).success(function(data){
            if('ok' === data){
                alert('身份验证已提交，我们会在1-2工作日内给到申请结果，请耐心等待管理员审核。');
                window.location.href = '/account/personalinfo.html';
            }else{
                alert('系统异常');
            }
        }).error(function(){
            alert('请检查您的网络');
        });
    }
</script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>
