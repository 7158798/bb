<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/fns.tld" prefix="fns" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="page"%>
<%--<%@ taglib uri="/WEB-INF/tlds/pagination.tld" prefix="page" %>--%>
<%@page trimDirectiveWhitespaces="true" %>
<%--<c:set var="cdn" value="//s1.zhgtrade.com"/>--%>
<%--<c:set var="cdn" value="//zhgtrade.oss-cn-qingdao.aliyuncs.com"/>--%>
<%--<c:set var="cdn" value="//s1.zhgtrade.com"/>--%>
<%--<c:set var="resources" value="${fns:getProperty('resourcesUrl')}"/>--%>
<%--<c:set var="resources" value="//zhgtrade.oss-cn-qingdao.aliyuncs.com"/>--%>
<c:set var="version" value="1.0.0"/>
<%
    try {
        String cdn = (String) pageContext.getAttribute("cdn");
        if (cdn == null) {
            if (request != null) {
                String agent = request.getHeader("user-agent");
                if (agent != null && agent.contains("IE 8.0")) {
                    pageContext.setAttribute("cdn", "//zhgtrade.oss-cn-qingdao.aliyuncs.com");
                    pageContext.setAttribute("resources", "//zhgtrade.oss-cn-qingdao.aliyuncs.com");
                } else {
                    pageContext.setAttribute("cdn", "//s1.zhgtrade.com");
                    pageContext.setAttribute("resources", com.ruizton.main.comm.ConstantMap.getProperty("resourcesUrl"));
                }
            }

        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>