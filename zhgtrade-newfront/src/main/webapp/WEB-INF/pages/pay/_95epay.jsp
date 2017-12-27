<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
    <style>
        .pay_wait{width: 100%;height: 50px;text-align: center;margin: 200px auto;font-size: 16px;font-family: tahoma,Microsoft YaHei,Arial,Helvetica,sans-serif;}
    </style>
</head>
<body>
<form id="payForm" action="https://www.95epay.cn/sslpayment" method="post">
    <c:forEach items="${payKeys}" var="payKey">
        <input type="hidden" name="${payKey.key}" value="${payKey.value}">
    </c:forEach>
</form>
<div class="pay_wait">
    <span>正在跳转中...</span>
</div>
<script type="text/javascript" src="${resources}/static/js/jquery/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${resources}/static/js/utils/popup_dialog.js"></script>
<script type="text/javascript">
    <c:choose>
        <c:when test="${200 == code}">
            document.getElementById('payForm').submit();
        </c:when>
        <c:when test="${2 == code}">
            alert('最小充值额不能小于${minVal}');
            window.close();
        </c:when>
        <c:when test="${-30 == code}">
            alert('充值额度已超出${maxRMB}，需要手持身份认证哦！');
            window.location.href = '/account/uploadIdentifyPic.html';
        </c:when>
    </c:choose>
</script>
</body>
</html>
