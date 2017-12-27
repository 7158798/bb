<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%--<%--%>
<%--String path = request.getContextPath();--%>
<%--String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;--%>
<%--%>--%>
<%@include file="include.inc.jsp" %>
<%--分页组件--%>
<%--后台传一个page_map--%>
<%--总页数total_page，当前页码cur_page--%>

<div class="page">
   <ul>
   		
		${pagin }
	</ul>
</div>
