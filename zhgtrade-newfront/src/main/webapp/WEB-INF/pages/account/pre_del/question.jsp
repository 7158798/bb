<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>众股 - 招股金服旗下加密数字资产交易平台</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit' />
    <meta name="keywords" content="${requestScope.constant['webinfo'].fdescription }">
    <meta name="description" content="${requestScope.constant['webinfo'].fkeywords }">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
    <link rel="stylesheet" href="${resources}/static/css/account.css"/>
</head>
<body>

<c:set var="menu_index" value="4"/>
<%@include file="../../common/header.jsp" %>
<link rel="stylesheet" href="${resources}/static/css/question.css">
<div class="center_page">
    <div class="account_nav">
        <a href="javascript:void(0)" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_blue">财务中心</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_gray">个人中心</a>
    </div>
    <%@ include file="../../common/account_left.jsp" %>

    <div class="account_right fl">
        <div class="initiateProblem">
            <span class="title">发起提问</span>
            <div class="specificContent clear">
                <ul>
                    <li>
                        <span class="c1"><span class="red">*</span>问题类型:</span>
								<span class="c2">
									<select onchange="javascript:changeQuestionType();" id="questionType">
                                        <option value="-1">---请选择问题类型---</option>
                                        <c:forEach items="${question_list }" var="v" varStatus="st">
                                            <option value="${st.index+1 }">${v }</option>
                                        </c:forEach>
                                    </select>
								</span>
                    </li>
                    <li style="display: none;" id="addressLi"><span class="c1"><span class="red">*</span>重复充值地址:</span><span
                            class="c2"><input type="text" onblur="trimValue(this);" name="" value=""
                                              class="blankInformation" id="address"></span><span style="color:red;"
                                                                                                 id="addressMsg">&nbsp;</span>
                    </li>
                    <li style="display: none;" id="amountLi"><span class="c1"><span class="red">*</span>重复数量:</span><span
                            class="c2"><input type="text" onblur="this.value = this.value.replace(/^\D|\D$/, '')"
                                              onkeyup="this.value=this.value=(function (a) {return a.length &gt; 1 ? a.shift().replace(/\D/g, '') + '.' + a.join('').replace(/\D/g, '').slice(0, 8) : a[0].replace(/\D/g,'');})(this.value.split('.'))"
                                              name="" value="" class="blankInformation" id="amount"></span><span
                            style="color:red;" id="amountMsg">&nbsp;</span></li>
                    <li>
                        <span style="float:left;" class="c1"><span class="red">*</span>问题描述:</span>
								<span style="float:left;" id="" class="c2 red">
									<span>
										<textarea onblur="trimValue(this);" rows="4" cols="50" class="textarea"
                                                  id="desc" required></textarea>
									</span>
								</span>
                    </li>
                    <li><span class="c1"><span class="red">*</span>姓名:</span><span class="c2"><input type="text"
                                                                                                     onblur="trimValue(this);"
                                                                                                     name=""
                                                                                                     value="${fuser.frealName }"
                                                                                                     class="blankInformation"
                                                                                                     id="name"></span></li>
                    <li><span class="c1"><span class="red">*</span>联系电话:</span><span class="c2"><input type="text"
                                                                                                       onkeyup="value=value.replace(/[^\d]/g,'')"
                                                                                                       name=""
                                                                                                       value="${fuser.ftelephone }"
                                                                                                       class="blankInformation"
                                                                                                       id="phone"></span>
                    </li>
                </ul>
            </div>
        </div>

        <div>
            <span style="color: red;padding-left: 210px;" id="errorMsg">&nbsp;</span><br>
            <span style="float: left;display: block; width: 70px">&nbsp;</span><a onclick="submitQuestion();" class="questionButtonblue" href="javascript:void(0)">提交问题</a>
        </div>
    </div>
</div>

</div>

<%@ include file="../../common/footer.jsp" %>
</body>
</html>

<script type="text/javascript">
    isForward();
</script>
<script src="${resources}/static/js/account/question.js"></script>