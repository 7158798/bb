<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改游戏规则信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateGameRule.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>会员等级：</dt>
				<dd>
					<input type="text" name="flevel" class="required digits"
						size="70" value="${gamerule.flevel }" readonly="true"/>
				</dd>
			</dl>
			
			<dl>
				<dt>升级消费币类型：</dt>
				<dd>
					<select
						type="combox" name="vid1">
						<c:forEach items="${allType}" var="type">
							<c:if test="${type.fid == gamerule.fupgradeCoinType.fid}">
								<option value="${type.fid}" selected="true">${type.fname}</option>
							</c:if>
							<c:if test="${type.fid != gamerule.fupgradeCoinType.fid}">
								<option value="${type.fid}">${type.fname}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>升级消耗数量：</dt>
				<dd>
					<input type="text" name="fupgradeNeedQty" class="required number"
						size="70" value="${gamerule.fupgradeNeedQty }" />
				</dd>
			</dl>
			<dl>
				<dt>升级所需推荐注册数：</dt>
				<dd>
					<input type="text" name="fupgradeNeedReQty" class="required digits"
						size="70" value="${gamerule.fupgradeNeedReQty }" />
				</dd>
			</dl>
			
			
			<dl>
				<dt>种植虚拟币类型：</dt>
				<dd>
					<input type="hidden" name="fid" value="${gamerule.fid }" /> <select
						type="combox" name="vid">
						<c:forEach items="${allType}" var="type">
							<c:if test="${type.fid == gamerule.fvirtualcointype.fid}">
								<option value="${type.fid}" selected="true">${type.fname}</option>
							</c:if>
							<c:if test="${type.fid != gamerule.fvirtualcointype.fid}">
								<option value="${type.fid}">${type.fname}</option>
							</c:if>
						</c:forEach>
					</select>
					<span>种植和收获虚拟币类型</span>
				</dd>
			</dl>
			<dl>
				<dt>种值消耗数量：</dt>
				<dd>
					<input type="text" name="fexpendQty" class="required number"
						size="70" value="${gamerule.fexpendQty }" />
					<span>指每一块土地，每一小时</span>	
				</dd>
			</dl>
			<dl>
				<dt>可种植时长：</dt>
				<dd>
					<input type="text" name="fcanZdtimes" class="required number"
						size="70" value="${gamerule.fcanZdtimes }" /><span>小时</span>
				</dd>
			</dl>
			<dl>
				<dt>每小时收获数量：</dt>
				<dd>
					<input type="text" name="fharvestQty" class="required number"
						size="70" value="${gamerule.fharvestQty }" />
					<span>指每一块土地，每一小时</span>	
				</dd>
			</dl>
			
			<dl>
				<dt>购买土地消耗币类型：</dt>
				<dd>
					<select
						type="combox" name="vid3">
						<c:forEach items="${allType}" var="type">
							<c:if test="${type.fid == gamerule.fvirtualcointype1.fid}">
								<option value="${type.fid}" selected="true">${type.fname}</option>
							</c:if>
							<c:if test="${type.fid != gamerule.fvirtualcointype1.fid}">
								<option value="${type.fid}">${type.fname}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>每一块消耗数量：</dt>
				<dd>
					<input type="text" name="fbuyQty" class="required number"
						size="70" value="${gamerule.fbuyQty }" />
				</dd>
			</dl>
			
			<dl>
				<dt>增产概率：</dt>
				<dd>
					<input type="text" name="fgoodRate" class="required number"
						size="70" value="${gamerule.fgoodRate }" />
				</dd>
			</dl>
			<dl>
				<dt>减产概率：</dt>
				<dd>
					<input type="text" name="fbadRate" class="required number"
						size="70" value="${gamerule.fbadRate }" />
				</dd>
			</dl>
			
			<dl>
				<dt>赠送土地：</dt>
				<dd>
					<input type="text" name="fsendqty" class="required digits"
						size="70" value="${gamerule.fsendqty }" />
				</dd>
			</dl>
			<dl>
				<dt>土地总数量：</dt>
				<dd>
					<input type="text" name="ftotalqty" class="required digits"
						size="70" value="${gamerule.ftotalqty }" />
				</dd>
			</dl>
			
			<dl>
				<dt>种子类型：</dt>
				<dd>
					<select type="combox" name="ftype">
					<c:forEach items="${typeMap}" var="t">
					<c:if test="${t.key == gamerule.ftype}">
				    	<option value="${t.key}" selected="true">${t.value}</option>
					</c:if>
					<c:if test="${t.key != gamerule.ftype}">
				    	<option value="${t.key}">${t.value}</option>
					</c:if>
					</c:forEach>
		            </select>
				</dd>
			</dl>
			
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div>
				</li>
				<li><div class="button">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div>
				</li>
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
