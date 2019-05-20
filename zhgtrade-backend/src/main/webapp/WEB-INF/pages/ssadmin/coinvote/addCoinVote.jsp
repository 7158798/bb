<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<h2 class="contentTitle">添加虚拟币类型信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/saveCoinVote.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>中文名称：</dt>
				<dd>
					<input type="text" name="fcnName" maxlength="30" class="required"
						size="70" />
				</dd>
			</dl>
			<dl>
				<dt>英文名称：</dt>
				<dd>
					<input type="text" name="fenName" maxlength="30"
						class="required" size="70" />
				</dd>
			</dl>
			<dl>
				<dt>简称：</dt>
				<dd>
					<input type="text" name="fshortName" maxlength="30" class="required"
						size="40" />
				</dd>
			</dl>
			<dl>
				<dt>链接：</dt>
				<dd>
					<input type="text" name="furl" maxlength="30"
						class="required" size="70" />
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
