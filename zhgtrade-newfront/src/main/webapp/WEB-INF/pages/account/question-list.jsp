<%--
  Created by IntelliJ IDEA.
  User: sunpeng
  Date: 2016/6/13 0013
  Time: 20:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<div class="fill_right account_questions">
    <!-- <h2 class="ml40">未解决</h2> -->
    <div class="title">
        <ul id="title" class="clear">
            <li class="unsolved db fl f16 fb ${type == 1 ? 'cur' : ""}">
                <a>未解决</a>
            </li>
            <li class="fl li_separator"></li>
            <li class="solved db fl f16 fb ${type == 2 ? 'cur' : ""}">
                <a>已解决</a>
            </li>
            <li class="fl li_separator"></li>
            <li class="fl f16 fb">
                <div class="send_message cp">
                   <%-- <i class="icon db fl"></i>--%>
                    <span class="db fl">提交工单</span>
                </div>
            </li>
        </ul>
    </div>

    <ul id="content" class="mt20">
        <li>
            <div class="content">
                <div class="Tenbody">
                    <div ></div>
                    <table class="questionlist">
                        <tr style="border-bottom: 1px solid #30c2ff30;">
                            <th width="80">问题编号</th>
                            <th width="110">问题类型</th>
                            <th width="200">问题描述</th>
                            <th width="150">问题回复</th>
                            <th width="150">提交时间</th>
                            <th width="80">问题状态</th>
                            <th>操作</th>
                        </tr>

                        <%--<tr><td colspan="7">您暂时没有提问记录</td></tr>--%>
                        <%--<tr>--%>
                        <%--<td>10010</td>--%>
                        <%--<td>充值问题</td>--%>
                        <%--<td>这是个问题，重点bug</td>--%>
                        <%--<td>感谢您的提问我们会加紧解决问题</td>--%>
                        <%--<td>2016-04-24 15:49:13</td>--%>
                        <%--<td>未解决</td>--%>
                        <%--<td>删除</td>--%>
                        <%--</tr>--%>
                        <c:forEach items="${list }" var="v" varStatus="vs">
                            <tr>
                                <td class="gray" width="80">${v.fid }</td>
                                <td class="gray" width="110">${v.ftype_s }</td>
                                <td class="gray desc singlerow" width="150"><c:out
                                        value="${v.fdesc }"/></td>
                                <td class="gray answer singlerow" width="150">
                                    <c:choose>
                                        <c:when test="${v.fanswer==''||v.fanswer==null}">无</c:when>
                                        <c:otherwise>${v.fanswer }</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="gray desc multirow dn" width="150"><c:out
                                        value="${v.fdesc }"/></td>
                                <td class="gray answer multirow dn" width="150">
                                    <c:choose>
                                        <c:when test="${v.fanswer==''||v.fanswer==null}">无</c:when>
                                        <c:otherwise>${v.fanswer }</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="gray" width="150"><fmt:formatDate value="${v.fcreateTime }"
                                                                             pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td class="gray" width="80">${v.fstatus_s }</td>
                                <td class="gray" width="60"><a
                                        onclick="javascript:cancelQuestion(${v.fid });"
                                        href="javascript:void(0);">删除</a></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${fn:length(list)==0 }">
                            <tr>
                                <td colspan="7">您暂时没有提问记录</td>
                            </tr>
                        </c:if>
                    </table>
                    <page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/question-list.html?type=${type}&currentPage=#pageNumber"/>
                </div>
            </div>
        </li>
    </ul>
    <input hidden type="hidden" id="currentPage" value="${currentPage}">
    <input hidden type="hidden" id="type" value="${type}">
    <!-- <div class="Tentitle"></div>
    <h2 class="ml40">已解决</h2> -->
</div>