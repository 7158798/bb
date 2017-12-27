<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit' />
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
</head>
<body>
<c:set var="menu_index" value="6"/>
<c:set var="nav_name" value="1"/>
<%@ include file="../common/header.jsp" %>
<link rel="stylesheet" href="${resources}/static/css/guide.css?v=1.2"/>
<div class="center_page">
    <div class="guide_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="/guide/help.html" class="f12 c_blue">新手指南</a>
        <i class="yjt">&gt;</i>
        <a class="c_gray">API</a>
    </div>
    <%@ include file="../common/guide_left.jsp" %>
    <div class="guide_right fl assets_center clear po_re zin70">
        <div class="content" style="margin-top: 1.8em">
            <h1>API</h1>
            <div class="api">
                <div class="about_text">
                    <div style="line-height:25px;font-size:14px;">
                        <div class="API-hover-color">
                            <p style="font-size:14px;"><a style="color:#ee5600;" href="#partone">一、API使用说明</a></p>
                            <p class="ml35"><a href="#one">1、请求过程说明</a></p>
                            <p class="ml35"><a href="#two">2、安全认证</a></p>
                            <p class="ml35"><a href="#three">3、返回结果说明</a></p>
                            <p class="ml35"><a href="#four">4、接口</a></p>
                            <p class="ml70"><a href="#four-one">4.1获取当前最新行情 - Ticker</a></p>
                            <p class="ml70"><a href="#four-two">4.2获取当前所有最新行情 - Tickers</a></p>
                            <p class="ml70"><a href="#four-three">4.3市场深度 - Depth</a></p>
                            <p class="ml70"><a href="#four-four">4.4最近的市场交易 - Orders</a></p>
                            <p class="ml70"><a href="#four-five">4.5账户信息 - Account Balance</a></p>
                            <p class="ml70"><a href="#four-six">4.6充值地址 - Wallet</a></p>
                            <p class="ml70"><a href="#four-seven">4.7挂单查询 - Trade_list</a></p>
                            <p class="ml70"><a href="#four-eight">4.8查询订单信息 - Trade_view</a></p>
                            <p class="ml70"><a href="#four-nine">4.9取消订单 - Cancel_trade</a></p>
                            <p class="ml70"><a href="#four-ten">4.10下单 - Trade</a></p>
                            <p class="ml35"><a href="#five">5.数字货币对照表</a></p>
                            <p class="ml35"><a href="#six">6.错误代码对照表</a></p>
                            <%--<p style="font-size:14px;"><a style="color:#ee5600;" href="#parttwo">二、API实例代码</a></p>--%>
                        </div>
                        <h2 style="font-weight: bold;padding-left:0;font-size: large;margin-top: 20px;" id="partone">一、API使用说明</h2>
                        <p id="one"><strong>1、请求过程说明</strong></p>
                        <p style="text-indent: 20px;">1.1 构造请求数据，用户数据按照招股提供的接口规则，通过程序生成签名和要传输给招股的数据集合；</p>
                        <p style="text-indent: 20px;">1.2 发送请求数据，把构造完成的数据集合通过POST/GET提交的方式传递给招股；</p>
                        <p style="text-indent: 20px;">1.3 招股对请求数据进行处理，服务器在接收到请求后，会首先进行安全校验，验证通过后便会处理该次发送过来的请求；</p>
                        <p style="text-indent: 20px;">1.4 返回响应结果数据，招股把响应结果以JSON的格式反馈给用户，具体的响应格式，错误代码参见接口部分；</p>
                        <p style="text-indent: 20px;">1.5 对获取的返回结果数据进行处理；</p>
                        <p id="two"><strong>2、安全认证</strong></p>
                        <p style="text-indent: 20px;">所有的private API都需要经过认证</p>
                        <p style="text-indent: 20px;">Api的申请可以到财务中心 -&gt; API，申请得到Key和密钥</p>
                        <p style="text-indent: 20px;">注意:请勿向任何人泄露这两个参数，这像您的密码一样重要</p>
                        <p style="padding-left: 20px;"><strong>签名机制</strong></p>
                        <p style="text-indent: 20px;">每次请求private api 都需要验证签名，发送的参数示例：</p>
                        <p style="text-indent: 20px;">$param = array(</p>
                        <p style="padding-left: 40px">amount =&gt; 1,</p>
                        <p style="padding-left: 40px;">price =&gt; 10000,</p>
                        <p style="padding-left: 40px;">type =&gt; 'buy',</p>
                        <p style="padding-left: 40px;">key =&gt; 'r5eb88920468848669b73b3afa8ac2053',</p>
                        <p style="padding-left: 40px;">sign =&gt; '459c69d25c496765191582d9611028b997483'</p>
                        <p style="padding-left: 20px;">);</p>
                        <p style="text-indent: 20px;">sign是签名，是将amount price type key secret等参数升序通过'&amp;'字符连接起来进行sha1算法加密得到的值.</p>
                        <p id="three"><strong>3、返回结果说明</strong></p>
                        <p style="padding-left: 20px;">返回结果为JSON数据结构，如果result字段为true，则请求处理成功，否则请通过resultCode字段的值对应错误代码对照表</p>
                        <p id="four"><strong>4、接口</strong></p>
                        <div class="aboutText">
                            <h2>只读权限方法列表(每秒最大请求次数为50)</h2>
                            <ul id="four-one">
                                <h3>Ticker（牌价）</h3>
                                <h4>Path：/api/v1/ticker</h4>
                                <h4>Request类型：GET</h4>
                                <h4>参数</h4>
                                <li>symbol - 1,2...(<a href="#five">货币对照表</a>)</li>
                                <h4>返回JSON dictionary</h4>
                                <li>high - 最高价</li>
                                <li>low  - 最低价</li>
                                <li>buy  - 买一价</li>
                                <li>sell - 卖一价</li>
                                <li>last - 最近一次成交价</li>
                                <li>vol  - 成交量</li>
                                <h4>返回结果示例：</h4>
                                <li>{"result":true,"ticker":{"high":0.0,"vol":0.0,"last":0.999,"low":0.0,"buy":0.0,"sell":0.0}}</li>
                            </ul>
                            <ul id="four-two">
                                <h3>Tickers（所有牌价）</h3>
                                <h4>Path：/api/v1/tickers</h4>
                                <h4>Request类型：GET</h4>
                                <h4>返回JSON dictionary</h4>
                                <li>symbol - 1,2...(<a href="#five">货币对照表</a>)</li>
                                <li>high - 最高价</li>
                                <li>low  - 最低价</li>
                                <li>buy  - 买一价</li>
                                <li>sell - 卖一价</li>
                                <li>last - 最近一次成交价</li>
                                <li>vol  - 成交量</li>
                                <h4>返回结果示例：</h4>
                                <li>{"result":true,"tickers":[{"symbol":1,"high":0.0,"vol":0.0,"last":0.999,"low":0.0,"buy":0.0,"sell":0.0}]}</li>
                            </ul>
                            <ul id="four-three">
                                <h3>Depth（市场深度）</h3>
                                <h5>描述：返回所有的市场深度，此回应的数据量会较大，所以请勿频繁调用。</h5>
                                <h4>Path：/api/v1/depth</h4>
                                <h4>Request类型：GET</h4>
                                <h4>参数</h4>
                                <li>symbol - 1,2...(<a href="#five">货币对照表</a>)</li>
                                <li>merge  - 小数位，默认为0(0,1,2,3,4)</li>
                                <h4>返回JSON dictionary</h4>
                                <li>asks - 委买单[价格, 委单量]，价格从高到低排序</li>
                                <li>bids - 委卖单[价格, 委单量]，价格从低到高排序</li>
                                <h4>返回结果示例：</h4>
                                <li>{"result":true,"asks":[[0.1,1873466.8251]],"bids":[[0.1,3104.8],[0.2,221557.14],[0.3,237821.0408],[0.4,384514.6972],[0.5,185843.2476],[0.6,25617.6598],[0.7,43260.6396],[0.8,133761.3651],[0.9,199071.0349]]}</li>
                            </ul>
                            <ul id="four-four">
                                <h3>Orders（市场交易）</h3>
                                <h5>描述：返回100个最近的市场交易，按时间倒序排列，此回应的数据量会较大，所以请勿频繁调用。</h5>
                                <h4>Path：/api/v1/orders</h4>
                                <h4>Request类型：GET</h4>
                                <h4>参数</h4>
                                <li>symbol - 1,2...(<a href="#five">货币对照表</a>)</li>
                                <li>size   - 记录数，默认100，最大100</li>
                                <h4>返回JSON dictionary</h4>
                                <li>date   - Unix时间戳(sec)</li>
                                <li>price  - 交易价格</li>
                                <li>amount - 交易数量</li>
                                <%--<li>tid - 交易ID</li>--%>
                                <li>type   - 交易类型</li>
                                <h4>返回结果示例：</h4>
                                <li>{"result":true,"orders":[{"date":1466047511,"amount":197.0472,"price":0.999,"type":"sell"},{"date":1466047295,"amount":4756.32,"price":1.0,"type":"sell"},{"date":1466047295,"amount":5000.95,"price":1.01,"type":"sell"},{"date":1466047295,"amount":95.95,"price":1.01,"type":"sell"},{"date":1464613942,"amount":28.8272,"price":0.1336,"type":"sell"},{"date":1464613930,"amount":64.1014,"price":0.1339,"type":"sell"},{"date":1464613913,"amount":74.4042,"price":0.1349,"type":"sell"},{"date":1464613941,"amount":28.8272,"price":0.1336,"type":"sell"}]}</li>
                            </ul>
                            <ul id="four-five">
                                <h3>Account Balance（账户信息）</h3>
                                <h5>列举您的帐户信息</h5>
                                <h4>Path：/api/v1/balance</h4>
                                <h4>Request类型：GET</h4>
                                <h4>参数</h4>
                                <h4>返回JSON dictionary</h4>
                                <li>cny_total  - 人民币总余额</li>
                                <li>cny_frozen - 人民币冻结余额</li>
                                <li>btc_total  - 比特币总余额</li>
                                <li>btc_frozen - 比特币冻结余额</li>
                                <li>............</li>
                                <h4>返回结果示例：</h4>
                                <li>{"result":true,"info":{"bkc_frozen":"0","bfc_total":"0","bgc_total":"906.0000","gyc_total":"453.0000","fac_frozen":"0","cic_total":"456.0000","cny_total":"46577954.58","mv000076_total":"0","blc_frozen":"0","lvc_frozen":"0","qec_total":"453.0000","spc_total":"906.0000","gpc_total":"0","bte_total":"906.0000","gyc_frozen":"453.0000","wwc_frozen":"0","eth_total":"453.0000","wlc_frozen":"0","mv000068_total":"906.0000","fac_total":"465.0000","gmc_frozen":"0","btc_total":"906.0000","spc_frozen":"453.0000","shp_frozen":"0","btc_frozen":"453.0000","bte_frozen":"453.0000","cny_frozen":"43459.10","shp_total":"45.0000","gmc_total":"45.0000","cic_frozen":"13.0000","qec_frozen":"453.0000","zgc_total":"10000004753.0000","eth_frozen":"0","wtc_frozen":"453.0000","mv000068_frozen":"453.0000","mv000076_frozen":"0","bfc_frozen":"0","gpc_frozen":"0","bgc_frozen":"453.0000","bkc_total":"46354.0000","blc_total":"453.0000","wlc_total":"0","lvc_total":"0","zgc_frozen":"4753.0000","wtc_total":"453.0000","wwc_total":"0"}}</li>
                            </ul>
                            <ul id="four-six">
                                <h3>Wallet（充值地址）</h3>
                                <h4>Path：/api/v1/wallet</h4>
                                <h4>Request类型：GET</h4>
                                <h4>参数</h4>
                                <li>symbol - 1,2...(<a href="#five">货币对照表</a>)</li>
                                <h4>返回JSON dicitionary</h4>
                                <li>result - true(成功), false(失败)</li>
                                <li>address - 充值地址</li>
                                <h4>返回结果示例：</h4>
                                <li>{"result":true,"address":"CJtCL4DyJTtWyPZRLTeU2DBBajaH8JyxcP"}</li>
                            </ul>
                            <ul id="four-seven">
                                <h3>Trade_list（挂单查询）</h3>
                                <h5>您指定时间后的挂单，可以根据类型查询，比如查看正在挂单和全部挂单</h5>
                                <h4>Path：/api/v1/trade_list</h4>
                                <h4>Request类型：GET</h4>
                                <h4>参数</h4>
                                <li>symbol - 1,2...(<a href="#five">货币对照表</a>)</li>
                                <li>since  - Unix时间戳(sec)，默认0</li>
                                <li>type   - 挂单类型[1:正在挂单, 0:所有挂单]</li>
                                <h4>返回JSON dictionary</h4>
                                <li>id          - 挂单ID</li>
                                <li>datetime    - date and time</li>
                                <li>type        - "buy" or "sell"</li>
                                <li>price       - price</li>
                                <li>amount      - 下单时数量</li>
                                <li>left_amount - 当前剩余数量</li>
                                <li>status      - 挂单状态[1：未成交，2：部分成交，3：完全成交，4：已撤销]</li>
                                <h4>返回结果示例：</h4>
                                <li>{"result":true,"orders":[{"datetime":"2016-06-17 15:43:10","amount":724.5825,"price":0.999,"id":6280572,"left_amount":0.0,"type":"sell","status":3},{"datetime":"2016-06-17 15:41:46","amount":1725.3078,"price":1.0,"id":6280571,"left_amount":725.3078,"type":"sell","status":4},{"datetime":"2016-06-17 15:33:44","amount":5277.5609,"price":1.01,"id":6280561,"left_amount":1742.5609,"type":"sell","status":4},{"datetime":"2016-06-17 15:31:34","amount":5329.814,"price":1.02,"id":6280557,"left_amount":5329.814,"type":"sell","status":4},{"datetime":"2016-06-17 15:29:39","amount":6063.978,"price":1.011,"id":6280555,"left_amount":5282.7862,"type":"sell","status":4},{"datetime":"2016-06-17 15:17:48","amount":6237.92,"price":1.04,"id":6280554,"left_amount":6237.92,"type":"sell","status":4},{"datetime":"2016-06-17 15:16:11","amount":6117.96,"price":1.02,"id":6280553,"left_amount":6117.96,"type":"sell","status":4},{"datetime":"2016-06-15 14:46:42","amount":7797.4,"price":1.3,"id":6278998,"left_amount":7797.4,"type":"sell","status":4}]}</li>
                            </ul>
                            <ul id="four-eight">
                                <h3>Trade_view（查询订单信息）</h3>
                                <h4>Path：/api/v1/trade_view</h4>
                                <h4>Request类型：GET</h4>
                                <h4>参数</h4>
                                <li>id - 挂单ID</li>
                                <h4>返回JSON dictionary</h4>
                                <li>id          - 挂单ID</li>
                                <li>datetime    - date and time</li>
                                <li>type        - "buy" or "sell"</li>
                                <li>price       - price</li>
                                <li>amount      - 下单时数量</li>
                                <li>left_amount - 当前剩余数量</li>
                                <li>status      - 挂单状态[1：未成交，2：部分成交，3：完全成交，4：已撤销]</li>
                                <h4>返回结果示例：</h4>
                                <li>{"result":true,"order":{"datetime":"2016-06-17 15:43:10","amount":724.5825,"price":0.999,"id":6280572,"left_amount":0.0,"type":"sell","status":3}}</li>
                            </ul>
                            <h2>完整权限方法列表(每秒最大请求数为10次)</h2>
                            <ul id="four-nine">
                                <h3>Trade_cancel（取消订单）</h3>
                                <h4>Path：/api/v1/cancel_trade</h4>
                                <h4>Request类型：POST</h4>
                                <h4>参数</h4>
                                <li>id - 挂单ID</li>
                                <h4>返回JSON dictionary</h4>
                                <li>result - true(成功), false(失败)</li>
                                <li>id - 订单ID</li>
                                <h4>返回结果示例：</h4>
                                <li>{"result":true, "id":"11"}</li>
                            </ul>
                            <ul id="four-ten">
                                <h3>Trade_add（下单）</h3>
                                <h4>Path：/api/v1/trade</h4>
                                <h4>Request类型：POST</h4>
                                <h4>参数</h4>
                                <li>symbol - 1,2...(<a href="#five">货币对照表</a>)</li>
                                <li>amount - 购买数量</li>
                                <li>price  - 购买价格</li>
                                <li>type   - "buy" or "sell"</li>
                                <h4>返回JSON dictionary</h4>
                                <li>id - 挂单ID</li>
                                <li>result - true(成功), false(失败)</li>
                                <h4>返回结果示例：</h4>
                                <li>{"result":true,"id":6294079}</li>
                            </ul>
                            <h2>数据类型</h2>
                            <ul>
                                <li>*_total - float</li>
                                <li>*_frozen - float</li>
                                <li>id - int</li>
                                <li>datetime - datetime</li>
                                <li>since - int</li>
                                <li>type - string</li>
                                <li>price - float</li>
                                <li>*amount - float</li>
                                <li>status - int</li>
                                <li>trade_id - int</li>
                                <li>fee - float</li>
                                <li>result - bool</li>
                                <li>message - string</li>
                                <li>address - string</li>
                            </ul>
                        </div>
                    </div>
                    <span id="five" style="height: 35px;line-height: 35px;font-size: 16px;font-weight: 700;margin: 10px 0px;">数字货币对照表</span>
                    <table>
                        <tbody>
                            <tr>
                                <th width="120">货币编号</th>
                                <th width="200">货币简称</th>
                                <th width="200">货币名称</th>
                            </tr>
                            <c:forEach items="${conis}" var="coin">
                                <tr>
                                    <td>${coin.fid}</td>
                                    <td>${fn:toLowerCase(coin.fShortName)}</td>
                                    <td>${coin.fname}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <span id="six" style="height: 35px;line-height: 35px;font-size: 16px;font-weight: 700;margin: 10px 0px;">错误代码对照表</span>
                    <table>
                        <tbody><tr>
                            <th width="120">错误代码</th>
                            <th width="200">详细描述</th>
                        </tr>
                        <tr>
                            <td>101</td>
                            <td>必填参数不能为空</td>
                        </tr>
                        <tr>
                            <td>102</td>
                            <td>API key不存在</td>
                        </tr>
                        <tr>
                            <td>103</td>
                            <td>API已禁止使用</td>
                        </tr>
                        <tr>
                            <td>104</td>
                            <td>权限已关闭</td>
                        </tr>
                        <tr>
                            <td>105</td>
                            <td>权限不足</td>
                        </tr>
                        <tr>
                            <td>106</td>
                            <td>签名不匹配</td>
                        </tr>
                        <tr>
                            <td>201</td>
                            <td>虚拟币不存在</td>
                        </tr>
                        <tr>
                            <td>202</td>
                            <td>虚拟币不能充值和提款</td>
                        </tr>
                        <tr>
                            <td>203</td>
                            <td>虚拟币还没分配到钱包地址</td>
                        </tr>
                        <tr>
                            <td>204</td>
                            <td>取消挂单失败（部分成交或全部已成交）</td>
                        </tr>
                        <tr>
                            <td>205</td>
                            <td>交易数量不能小于0.0001</td>
                        </tr>
                        <tr>
                            <td>206</td>
                            <td>交易价格不能小于0.0001</td>
                        </tr>
                        <tr>
                            <td>207</td>
                            <td>虚拟币未开放交易</td>
                        </tr>
                        <tr>
                            <td>208</td>
                            <td>人民币余额不足</td>
                        </tr>
                        <tr>
                            <td>209</td>
                            <td>交易密码错误</td>
                        </tr>
                        <tr>
                            <td>210</td>
                            <td>交易价格不在限价区间内</td>
                        </tr>
                        <tr>
                            <td>211</td>
                            <td>虚拟币余额不足</td>
                        </tr>
                        <tr>
                            <td>212</td>
                            <td>最大交易总金额不能大于50000</td>
                        </tr>
                        <tr>
                            <td>401</td>
                            <td>非法参数</td>
                        </tr>
                        <tr>
                            <td>402</td>
                            <td>系统异常</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="cb"></div>
<%@ include file="../common/footer.jsp" %>
</body>
</html>