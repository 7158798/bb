<%--
  Created by IntelliJ IDEA.
  User: sunpeng
  Date: 2016/5/20 0020
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib  uri ="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="guide_left fl">
    <c:forEach var="ftype" items="${farticletypes}" varStatus="status">
        <div class="menu <c:if test="${ftype.fid == typeId}">cur
        <c:choose>
            <c:when test="${status.first}">
            bb1
            </c:when>
            <c:when test="${status.last}">
            btb1
            </c:when>
            <c:otherwise>
            btb1
            </c:otherwise>
        </c:choose>
        </c:if>">
            <li class="level1">
                <a href="/guide/index.html?id=${ftype.fid}">
                    <span>${ftype.fname}</span>
                    <i class="dn iconfont c_blue fl">&#xe60c;</i>
                </a>
            </li>
            <div class="level2">
                <c:if test="${ftype.fid == typeId}">
                    <c:forEach var="v" items="${articleSubling}" varStatus="status">
                        <a class="<c:if test="${v.fid == farticle.fid}">c_blue</c:if>"
                           href="/guide/article.html?id=${v.fid}" title="${v.ftitle}">${v.ftitle}</a>
                    </c:forEach>
                    <c:if test="${count > 5}">
                        <a class="more c_blue" href="/guide/index.html?id=${farticletype.fid}">更多...</a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </c:forEach>
    <div class="menu <c:if test='${nav_name==1}'> bt1 cur</c:if>">
    <li class="level1">
        <a href="/guide/api.html">
            <span>API</span>
            <i class="dn iconfont c_blue fl">&#xe60c;</i>
        </a>
    </li>
    </div>
    <div class="search">
        <input type="text" class="db fl" name="keyword" value="${fn:escapeXml(keyword)}" placeholder="搜索" />
        <span class="db fl" id="search">搜索</span>
    </div>
    <script>
        $(function(){
            var $input = $("input[name='keyword']");
            $input.focus();
            $input.val($input.val());
            $("#search").on("click",function(){
               var keyword=$(this).prev("input").val();
                if(isEmpty(keyword)){
                    $("input[name='keyword']").focus();
                    return;
                }
               location.href="/guide/result.html?keyword="+encodeURIComponent((keyword.replace(/\s+/g," ").replace(/^\s|\s$/g,"")));
            });
            $input.keyup(function(e){
               if(e.keyCode==13){
                   $("#search").trigger("click");
               }
            });
        });
    </script>
</div>
