<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/activity.tld" prefix="yj" %>
<div class="news">
    <div class="title b_l_blue pl10 f16">${fname}市场动态</div>
    <div class="content clear">
        <c:choose>
            <c:when test="${list!=null&&list.size()>0}">
            <c:forEach var="article" items="${list}" varStatus="v">
            <div class="col">
                <p>
                    <a href="/guide/article.html?id=${article.fid}" class="ellipsis" title="${article.ftitle}">${article.ftitle}</a>
                    <span class="db fr">${yj:formatDate(article.fcreateDate,"yyyy-MM-dd")}</span>
                </p>
            </div>
            </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="tac">暂无动态</div>
            </c:otherwise>
        </c:choose>
        <div style="height:40px;margin-top:40px;margin-right:10px;"><page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="javascript:void(0)"/></div>
    </div>
</div>
<script>
    $(function(){
        $(".page").find("a").on("click",function(){
            var pageNum=$(this).data("pagenum");
            $("#news-div").load("/market/news.html?currentPage="+pageNum+"&symbol="+${symbol});
        });
    });
</script>