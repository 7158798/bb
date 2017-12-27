<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html>
<head> 
 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="../comm/link.inc.jsp" %>
</head>
<body >
	
<%@include file="../comm/header.jsp" %>

<div class="marketArea clear">

<%@include file="../comm/left_menu.jsp" %>
<div class="rightArea">
			
						<div>	
				<div class="entrustTen">
					<div class="Tentitle">
						<span class="lightblue4">${fgame.fname }</span>
					</div>
				</div>
				<div class="coinBox lasttenorder">
					<div class="coinBoxBody2">
						<div class="coinbody">
							<div class="coinleft">
					        	<img alt="" src="${staticFileUrl}/static/front/images/safe/farm.jpeg">
					        </div>
							<div class="coinright"> 
								 <h4>游戏规则</h4>
								 <ul>
									  <li class="c-left">
										  <p>${fgame.fdescription }</p>
									  </li>
									  <li style="float:right;text-align:right;padding-right:10px;">
										  	<a style="margin-top: 7px;" title="进入" href="/gamecenter/game.html" class="button1-background button1">进入</a>
									  </li>
								  </ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			
		<%-- 	<div>	
				<div class="entrustTen">
					<div class="Tentitle">
						<span class="lightblue4">疯狂寻宝</span>
					</div>
				</div>
				<div class="coinBox lasttenorder">
					<div class="coinBoxBody2">
						<div class="coinbody">
							<div class="coinleft">
					        	<img alt="" src="${staticFileUrl}/static/front/images/safe/popcorn.jpeg">
					        </div>
							<div class="coinright"> 
								 <ul>
								  <h4>游戏规则</h4>
									  <li class="c-left">
										  <p>${popcorn.ftitle }</p>
									  </li>
									  <li style="float:right;text-align:right;padding-right:10px;">
										  	<a style="margin-top: 7px;" title="进入" href="/gamecenter/popcorn.html?type=1" class="button1-background button1">进入</a>
									  </li>
								  </ul>
							</div>
						</div>
					</div>
				</div>
			</div> --%>
			
			<div>	
				<div class="entrustTen">
					<div class="Tentitle">
						<span class="lightblue4">欢乐砸金蛋</span>
					</div>
				</div>
				<div class="coinBox lasttenorder">
					<div class="coinBoxBody2">
						<div class="coinbody">
							<div class="coinleft">
					        	<img alt="" src="${staticFileUrl}/static/front/images/safe/egg.jpeg">
					        </div>
							<div class="coinright"> 
								 <ul>
								  <h4>游戏规则</h4>
									  <li class="c-left">
										  <p>${lotteryRule.ftitle }</p>
									  </li>
									  <li style="float:right;text-align:right;padding-right:10px;">
										  	<a style="margin-top: 7px;" title="进入" href="/gamecenter/goldenEgg.html" class="button1-background button1">进入</a>
									  </li>
								  </ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			
		</div>

</div>

	
<%@include file="../comm/footer.jsp" %>	
</body>
</html>

<script type="text/javascript">
	isForward();
</script>	   