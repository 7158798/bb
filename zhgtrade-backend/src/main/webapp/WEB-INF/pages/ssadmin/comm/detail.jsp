<%@ page pageEncoding="UTF-8"%>
<div class="accordion" fillSpace="sidebar">
	<shiro:hasPermission name="user">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>会员管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/userList.html">
					<li><a href="ssadmin/userList.html" target="navTab"
						rel="userList">会员列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/userAuditList.html">
					<li><a href="ssadmin/userAuditList.html" target="navTab"
						rel="userAuditList">待审核会员列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/logList.html">
					<li><a href="ssadmin/logList.html" target="navTab"
						rel="logList">会员操作日志列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/entrustlogList.html">
					<li><a href="ssadmin/entrustlogList.html" target="navTab"
						rel="entrustlogList">会员交易记录列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/subscriptionList1.html">
					<li><a href="ssadmin/subscriptionList1.html" target="navTab"
						rel="subscriptionList1">人民币认购列表</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/subscriptionList.html">
					<li><a href="ssadmin/subscriptionList.html" target="navTab"
						rel="subscriptionList">换购列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/vouchersList.html">
					<li><a href="ssadmin/vouchersList.html" target="navTab"
						rel="vouchersList">会员代金券列表</a>
					</li>
				</shiro:hasPermission>
				<!-- <shiro:hasPermission name="ssadmin/vouchersList.html"> -->
					
				<!-- </shiro:hasPermission> -->
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="farm">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>农场管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/gameList.html">
					<li><a href="ssadmin/gameList.html" target="navTab"
						rel="gameList">游戏信息列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/gameRuleList.html">
					<li><a href="ssadmin/gameRuleList.html" target="navTab"
						rel="gameRuleList">游戏规则列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/gameLogList.html">
					<li><a href="ssadmin/gameLogList.html" target="navTab"
						rel="gameLogList">游戏记录列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/gameoperatelogList.html">
					<li><a href="ssadmin/gameoperatelogList.html" target="navTab"
						rel="gameoperatelogList">游戏操作记录列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>


	<shiro:hasPermission name="article">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>文章管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/articleList.html">
					<li><a href="ssadmin/articleList.html" target="navTab"
						rel="articleList">文章列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/articleTypeList.html">
					<li><a href="ssadmin/articleTypeList.html" target="navTab"
						rel="articleTypeList">文章类型</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/ssadmin/blockArticleList.html">
					<li><a href="/ssadmin/blockArticleList.html" target="navTab"
						rel="blockArticleList">区块达客文章列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/ssadmin/blockArticleTypeList.html">
					<li><a href="/ssadmin/blockArticleTypeList.html" target="navTab"
						rel="blockArticAleTypeList">区块达客文章类型</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="information">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>资讯平台
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="/ssadmin/informationArticleList.html">
					<li><a href="/ssadmin/informationArticleList.html" target="navTab"
						rel="informationArticAleList">文章列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/ssadmin/informationVentureArticleList.html">
					<li><a href="/ssadmin/informationVentureArticleList.html" target="navTab"
						rel="informationVentureArticAleList">创投圈文章</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/ssadmin/informationArticleTypeList.html">
					<li><a href="/ssadmin/informationArticleTypeList.html" target="navTab"
						rel="informationArticAleTypeList">文章类型</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/ssadmin/layoutList.html">
					<li><a href="/ssadmin/layoutList.html" target="navTab"
						rel="layoutList">首页推荐</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/ssadmin/keywordList.html">
					<li><a href="/ssadmin/keywordList.html" target="navTab"
						rel="keywordListList">关键字列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/projectList.html">
					<li><a href="/ssadmin/projectList.html" target="navTab"
						rel="projectList">项目列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/ssadmin/informationArgsList.html">
					<li><a href="/ssadmin/informationArgsList.html" target="navTab"
						rel="informationArgsList">站点设置</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/chitChatList.html">
					<li><a href="/ssadmin/chitChatList.html" target="navTab"
						rel="chitChatList">七嘴八舌列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/sensitiveWords.html">
					<li><a href="/ssadmin/sensitiveWords.html" target="navTab"
						   rel="sensitiveWords">敏感词汇列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/ssadmin/roadShowList.html">
					<li><a href="/ssadmin/roadShowList.html" target="navTab"
						rel="roadShowList">路演列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/ssadmin/roadShowList.html">
					<li><a href="/ssadmin/roadShowList.html?checkStatus=1&isAudit=true" target="navTab"
						rel="roadShowAuditList">路演待审核列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/contributionList.html">
					<li><a href="/ssadmin/contributionList.html" target="navTab"
						   rel="contributionList">投稿列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="virtualCoin">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>虚拟币管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/virtualCoinTypeList.html">
					<li><a href="ssadmin/virtualCoinTypeList.html" target="navTab"
						rel="virtualCoinTypeList">虚拟币类型列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/virtualCoinOrder.html">
					<li><a href="ssadmin/virtualCoinOrder.html" target="navTab"
						rel="virtualOrderList">虚拟币排序列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/withdrawFeesList.html">
					<li><a href="ssadmin/withdrawFeesList.html" target="navTab"
						rel="withdrawFeesList" title="人民币提现手续费列表">人民币提现手续费列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/walletAddressList.html">
				<li><a href="ssadmin/walletAddressList.html" target="navTab"
					rel="walletAddressList" title="虚拟币可用地址列表">虚拟币可用地址列表</a>
				</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/coinDetailList.html">
				<li><a href="ssadmin/coinDetailList.html" target="navTab"
					rel="coinDetailList" title="虚拟币详情列表">虚拟币详情列表</a>
				</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/limittradeList.html">
					<li><a href="ssadmin/limittradeList.html" target="navTab"
						   rel="limittradeList">限价交易列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/userVirtualCoinAddressList.html">
					<li><a href="ssadmin/userVirtualCoinAddressList.html" target="navTab"
						   rel="userVirtualCoinAddressList">用户地址列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="shop">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>商城管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/goodtypeList.html">
					<li><a href="ssadmin/goodtypeList.html" target="navTab"
						rel="goodtypeList">商品类型列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/goodsList.html">
					<li><a href="ssadmin/goodsList.html" target="navTab"
						rel="goodsList">商品列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/shoppinglogList.html">
					<li><a href="ssadmin/shoppinglogList.html" target="navTab"
						rel="shoppinglogList">成交记录列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="newCoin">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>新币投票管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/coinVoteList.html">
					<li><a href="/ssadmin/coinVoteList.html" target="navTab"
						rel="coinVoteList">虚拟币列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/coinVoteLogList.html">
					<li><a href="/ssadmin/coinVoteLogList.html" target="navTab"
						rel="coinVoteLogList">投票记录列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>
	
	<shiro:hasPermission name="hedging">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>对冲管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/hedginginfoList.html">
					<li><a href="/ssadmin/hedginginfoList.html" target="navTab"
						rel="hedginginfoList">对冲信息列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/hedgingList.html">
					<li><a href="/ssadmin/hedgingList.html" target="navTab"
						rel="hedgingList">对冲设置列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/hedginglogList.html">
					<li><a href="/ssadmin/hedginglogList.html" target="navTab"
						rel="hedginglogList">对冲记录列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/hedginguserlogList.html">
					<li><a href="/ssadmin/hedginguserlogList.html" target="navTab"
						rel="hedginguserlogList">会员投注列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>
	<shiro:hasPermission name="coinBirthCoin">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>币生币理财管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/balanceList.html">
					<li><a href="ssadmin/balanceList.html" target="navTab"
						rel="balanceList">定存信息列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/balancetypeList.html">
				<li><a href="ssadmin/balancetypeList.html" target="navTab"
					rel="balancetypeList">定存类型</a>
				</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/balancelogList.html">
				<li><a href="ssadmin/balancelogList.html" target="navTab"
					rel="balancelogList">会员定存记录类型</a>
				</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/balancelogReportList.html">
				<li><a href="ssadmin/balancelogReportList.html" target="navTab"
					rel="balancelogReportList">会员定存汇总表</a>
				</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="capital">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>资金管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/capitaloperationList.html">
					<li><a href="ssadmin/capitaloperationList.html"
						target="navTab" rel="capitaloperationList">人民币操作记录列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/capitalInList.html">
					<li><a href="ssadmin/capitalInList.html" target="navTab"
						rel="capitalInList">待审核人民币充值列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/capitalOutList.html">
					<li><a href="ssadmin/capitalOutList.html" target="navTab"
						rel="capitalOutList">待审核人民币提现列表</a>
					</li>
				</shiro:hasPermission>

				<shiro:hasPermission name="ssadmin/virtualCaptualoperationList.html">
					<li><a href="ssadmin/virtualCaptualoperationList.html"
						target="navTab" rel="virtualCaptualoperationList">虚拟币操作记录列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/virtualCapitalInList.html">
					<li><a href="ssadmin/virtualCapitalInList.html"
						target="navTab" rel="virtualCapitalInList">虚拟币充值列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/virtualCapitalOutList.html">
					<li><a href="ssadmin/virtualCapitalOutList.html"
						target="navTab" rel="virtualCapitalOutList">待审核虚拟币提现列表</a>
					</li>
				</shiro:hasPermission>


				<shiro:hasPermission name="ssadmin/walletList.html">
					<li><a href="ssadmin/walletList.html" target="navTab"
						rel="walletList">会员资金列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/virtualwalletList.html">
					<li><a href="ssadmin/virtualwalletList.html" target="navTab"
						rel="virtualwalletList">会员虚拟币列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/operationLogList.html">
					<li><a href="ssadmin/operationLogList.html" target="navTab"
						rel="operationLogList">人民币手工充值列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/virtualoperationlogList.html">
					<li><a href="ssadmin/virtualoperationlogList.html"
						target="navTab" rel="virtualoperationlogList">虚拟币手工充值列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/entrustList.html">
					<li><a href="ssadmin/entrustList.html" target="navTab"
						rel="entrustList">委托交易列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/entrustPlanList.html">
					<li><a href="ssadmin/entrustPlanList.html" target="navTab"
						rel="entrustPlanList">计划委托列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/walletErrorList.html">
				<li><a href="ssadmin/walletErrorList.html" target="navTab"
					   rel="walletErrorList">会员资金警报</a>
				</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/walletTransferList.html">
					<li><a href="/ssadmin/walletTransferList.html" target="navTab"
						   rel="transferRecordList">资金转移列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="question">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>提问管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/questionList.html">
					<li><a href="ssadmin/questionList.html" target="navTab"
						rel="questionList">提问记录列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/questionForAnswerList.html">
					<li><a href="ssadmin/questionForAnswerList.html"
						target="navTab" rel="questionForAnswerList">待回复提问列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="report">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>报表统计
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/userReport.html">
					<li><a
						href="ssadmin/userReport.html?startDate=<%=startDate%>&endDate=<%=endDate%>"
						target="navTab" rel="userReport">会员注册统计表</a>
					</li>
				</shiro:hasPermission>
				<%--<shiro:hasPermission name="ssadmin/capitaloperationReport.html">
					<li><a
						href="ssadmin/capitaloperationReport.html?type=1&status=3&url=ssadmin/capitaloperationReport&startDate=<%=startDate%>&endDate=<%=endDate%>"
						target="navTab" rel="capitaloperationReport">人民币充值统计表</a>
					</li>
				</shiro:hasPermission>--%>
				<shiro:hasPermission name="ssadmin/capitalInReport.html">
					<li><a href="/ssadmin/capitalInReport.html" target="navTab"
						   rel="capitalInReport">人民币充值报表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/capitaloperationOutReport.html">
					<li><a
							href="ssadmin/capitaloperationReport.html?type=2&status=3&url=ssadmin/capitaloperationOutReport&startDate=<%=startDate%>&endDate=<%=endDate%>"
							target="navTab" rel="capitaloperationOutReport">人民币提现统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/transferInReport.html">
					<li><a href="/ssadmin/transferInReport.html" target="navTab"
						   rel="transferInReport">转入资金报表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/vcOperationInReport.html">
					<li><a
						href="ssadmin/vcOperationReport.html?type=1&status=3&url=ssadmin/vcOperationInReport&startDate=<%=startDate%>&endDate=<%=endDate%>"
						target="navTab" rel="vcOperationInReport">虚拟币充值统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/vcOperationOutReport.html">
					<li><a
						href="ssadmin/vcOperationReport.html?type=2&status=3&url=ssadmin/vcOperationOutReport&startDate=<%=startDate%>&endDate=<%=endDate%>"
						target="navTab" rel="vcOperationOutReport">虚拟币提现统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/totalReport.html">
					<li><a href="ssadmin/totalReport.html" target="navTab"
						rel="totalReport">综合统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/walletReport.html">
					<li><a href="ssadmin/walletReport.html" target="navTab" rel="walletReport">钱包统计表</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/curEntrustReport.html">
					<li><a href="ssadmin/curEntrustReport.html" target="navTab" rel="curEntrustReport">当前委托统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/totalreportList.html">
					<li><a href="ssadmin/totalreportList.html" target="navTab"
						rel="totalreportList">平台资金汇总表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/viCoinTradeReport.html">
					<li><a href="/ssadmin/viCoinTradeReport.html" target="navTab"
						rel="viCoinTradeReport">虚拟币交易统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/upanddownReport.html">
					<li><a href="/ssadmin/upanddownReport.html" target="navTab"
						rel="viCoinTradeReport">涨跌幅统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/turnoverReport.html">
					<li><a href="/ssadmin/turnoverReport.html" target="navTab"
						rel="turnoverReport">交易额统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/dateWalletReport.html">
					<li><a href="/ssadmin/dateWalletReport.html" target="navTab"
						   rel="dateWalletReport">单日沉淀资金报表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/withdrawfeeReport.html">
					<li><a href="/ssadmin/withdrawfeeReport.html" target="navTab"
						   rel="withdrawfeeReport">提现手续费报表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/tradefeeReport.html">
					<li><a href="/ssadmin/tradefeeReport.html" target="navTab"
						   rel="tradefeeReport">交易手续费报表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="Api">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>API管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/apiList.html">
					<li><a href="ssadmin/apiList.html" target="navTab"
						   rel="apiList">API列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/apiRecord.html">
					<li><a href="ssadmin/apiRecord.html" target="navTab"
						   rel="apiRecord">API调用记录</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="system">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>系统管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/linkList.html">
					<li><a href="ssadmin/linkList.html" target="navTab"
						rel="linkList">友情链接列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/qqList.html">
					<li><a href="ssadmin/qqList.html" target="navTab" rel="qqList">QQ群列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/systemArgsList.html">
					<li><a href="ssadmin/systemArgsList.html" target="navTab"
						rel="systemArgsList">系统参数列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/systemBankList.html">
					<li><a href="ssadmin/systemBankList.html" target="navTab"
						rel="systemBankList">银行帐户列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/aboutList.html">
					<li><a href="ssadmin/aboutList.html" target="navTab"
						rel="aboutList">关于我们</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/securityTreeList.html">
					<li><a
						href="ssadmin/goSecurityJSP.html?url=ssadmin/securityTreeList&treeId=1"
						target="navTab" rel="securityTreeList">权限列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/roleList.html">
					<li><a href="ssadmin/roleList.html" target="navTab"
						rel="roleList">角色列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/adminList.html">
					<li><a href="ssadmin/adminList.html" target="navTab"
						rel="adminList">管理员列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/countLimitList.html">
					<li><a href="ssadmin/countLimitList.html" target="navTab"
						rel="countLimitList">限制管理列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>
	<shiro:hasPermission name="activity">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>活动管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/newYearRedWrapperDetail.html">
					<li><a href="ssadmin/newYearRedWrapperDetail.html" target="navTab"
						   rel="newYearRedWrapperDetail">新春红包分配情况</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/newYearRedWrapperList.html">
					<li><a href="ssadmin/newYearRedWrapperList.html" target="navTab"
						   rel="newYearRedWrapperList">新春红包分配列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>
	<shiro:hasPermission name="block">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>区块达客
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/blockAgentList.html">
					<li><a href="ssadmin/blockAgentList.html" target="navTab"
						   rel="blockAgentList">代理申请列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>
</div>