<!-- 消息中心author:xxp 2016-04-24 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<div class="fill_right account_newscenter">
    <h1 class="ml40 f22" style="margin-top: 1em;">消息列表</h1>
    <div class="news_operation f12">
        <a href="javascript:void(0)" id="delete_news">删除</a>
        <a href="javascript:void(0)" id="flag_all">全部标记为已读</a>
    </div>
    <div class="content">
        <div class="Tenbody">
            <div style="border-bottom:1px dashed #000000;"></div>
            <table id="news">
                <tr style="border-bottom: 1px solid #30c2ff30;">
                    <th width="50" id="selectAll" style="font-weight: 400"><i class="iconfont c_gray cp">&#xe620;</i><i class="iconfont c_blue cp dn">&#xe621;</i></th>
                    <th width="80">状态</th>
                    <th width="240">标题</th>
                    <th width="350">内容</th>
                    <th>时间</th>
                </tr>
                <tr style="height: 1em"></tr>
                <c:forEach items="${list }" var="v" varStatus="vs">
                    <tr id="message${v.fid}">
                        <td class="selectDeleteNew">
                            <span style="display: none;">${v.fid}</span>
                            <i class="noselect iconfont c_gray cp">&#xe620;</i>
                            <i class="select iconfont c_blue cp dn">&#xe621;</i>
                        </td>
                        <td style="cursor: pointer" onclick="javascript:messagedetail(${v.fid})">
                            <i class="iconfont ${v.fstatus == 1 ? 'c_blue': 'c_gray'}">&#xe61e;</i>
                        </td>
                        <td style="cursor: pointer;padding:0 20px;" title="${v.ftitle_short }" onclick="javascript:messagedetail(${v.fid})">${v.ftitle_short }</td>
                        <td style="cursor: pointer;padding:0 20px;" title="${v.fcontent}" class="content" onclick="javascript:messagedetail(${v.fid})">${v.fcontent}</td>
                        <td class="gray"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${v.fcreateTime }"/></td>
                    </tr>
                </c:forEach>
                <c:if test="${fn:length(list)==0 }">
                    <c:choose>
                        <c:when test="${type==1 }">
                            <tr>
                                <td colspan="6">您暂时没有未读消息</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6">您暂时没有已读消息</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </table>
            <page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/message-list.html?currentPage=#pageNumber"/>
        </div>
    </div>
</div>