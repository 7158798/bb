<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改游戏信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateGame.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>游戏简介：</dt>
				<dd>
				    <input type="hidden" value="${game.fid }" name="fid"/>
					<textarea class="required" name="fdescription" rows="4" cols="70">${game.fdescription}</textarea>
				</dd>
			</dl>
			<dl>
				<dt>最小收获时间：</dt>
				<dd>
					<input type="text" name="fminHarvestTime" class="required number"
						size="70" value="${game.fminHarvestTime }" /><span>小时</span>
				</dd>
			</dl>
			<dl>
				<dt>变草时长：</dt>
				<dd>
					<input type="text" name="fgrassTime" class="required number"
						size="70" value="${game.fgrassTime }" /><span>小时</span>
				</dd>
			</dl>
			<dl>
				<dt>开启消耗币类型：</dt>
				<dd>
					<select
						type="combox" name="vid1">
						<c:forEach items="${allType}" var="type">
							<c:if test="${type.fid == game.fvirtualcointype.fid}">
								<option value="${type.fid}" selected="true">${type.fname}</option>
							</c:if>
							<c:if test="${type.fid != game.fvirtualcointype.fid}">
								<option value="${type.fid}">${type.fname}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>开启消耗币数量：</dt>
				<dd>
					<input type="text" name="fqty" class="required number"
						size="70" value="${game.fqty }" /><span>个</span>
				</dd>
			</dl>
			<dl>
				<dt>开启冻结币类型：</dt>
				<dd>
					<select
						type="combox" name="vid2">
						<c:forEach items="${allType}" var="type">
							<c:if test="${type.fid == game.ffrozenvirtualcointype.fid}">
								<option value="${type.fid}" selected="true">${type.fname}</option>
							</c:if>
							<c:if test="${type.fid != game.ffrozenvirtualcointype.fid}">
								<option value="${type.fid}">${type.fname}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>开启冻结币数量：</dt>
				<dd>
					<input type="text" name="ffrozenqty" class="required number"
						size="70" value="${game.ffrozenqty }" /><span>个</span>
				</dd>
			</dl>
			<dl>
				<dt>除草消耗币类型：</dt>
				<dd>
					<select
						type="combox" name="vid3">
						<c:forEach items="${allType}" var="type">
							<c:if test="${type.fid == game.fgrassvirtualcointype.fid}">
								<option value="${type.fid}" selected="true">${type.fname}</option>
							</c:if>
							<c:if test="${type.fid != game.fgrassvirtualcointype.fid}">
								<option value="${type.fid}">${type.fname}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>除草消耗币数量：</dt>
				<dd>
					<input type="text" name="fgrassqty" class="required number"
						size="70" value="${game.fgrassqty }" /><span>个</span>
				</dd>
			</dl>
			<dl>
				<dt>有效天数：</dt>
				<dd>
					<input type="text" name="fdays" class="required digits"
						size="70" value="${game.fdays }" /><span>天</span>
				</dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div></li>
				<li><div class="button">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div></li>
			</ul>
		</div>
	</form>

</div>


<script type="text/javascript">
function customvalidXxx(element){
	if ($(element).val() == "xxx") return false;
	return true;
}
</script>
