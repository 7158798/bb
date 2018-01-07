!(function(){

    String.prototype.trimRight = function(charlist) {
        if (charlist === undefined)
            charlist = "\s";
        return this.replace(new RegExp("[" + charlist + "]+$"), "");
    };

    String.prototype.exp = function(){
        var result = this.toString();
        if (this.toString().split("-").length > 1) {
            var feeExp = this.toString().replace(".","")
            var feeFixed = "";
            for (var x = 0; x < this.toString().split("-")[1]; x++) {
                if (x != 0) {
                    feeFixed += "0"
                }
                else{
                    feeFixed += "0."
                }
            }
            if (feeFixed.split(".").length > 1) {
                if (feeFixed.split(".")[1].length < 8) {
                    for (var x = 0; x < 9 - feeFixed.split(".")[1].length; x++) {
                        feeFixed += feeExp[x]
                    }
                }
            }
            result = feeFixed;
        }
        return result;
    }
})()

!(function(){
    //var host = '192.168.0.168';
    var host = location.host;

    var WARN_TEXT = {
        '-1': lang.minTxNumber,
        '-2': lang.pwdError,
        '-3': lang.offerNumber,
        '-4': lang.sufficient,
        '-5': lang.tradePwdSet,
        '-6': lang.priceOver,
        '-8': lang.tradePwdEnter,
        '-100': lang.noCoinTips,
        '-101': lang.pauseExchange,
        '-400': lang.offTime,
        '-50': lang.tradePwdEmpty,
        '-200': lang.netTimeout,
        '-900': lang.priceLarge,
        '-111': lang.minAmountTrade,
        '-112': lang.tooLittle,
        '-113': lang.numberTooLow,
        '-35': lang.singleAmount,
        '-19': lang.priceDiff
    };

    var app = angular.module('app', ['i18n', 'ng-inputdecimalseparator']);

    app.filter('html', ['$sce', function ($sce) {
        return function (text) {
            return $sce.trustAsHtml(text);
        };
    }])

    app.filter('orderObjectBy', function() {
        return function (items, field, reverse) {
            function isNumeric(n) {
                return !isNaN(parseFloat(n)) && isFinite(n);
            }
            var filtered = [];
            angular.forEach(items, function(item, key) {
                item.key = key;
                filtered.push(item);
            });
            function index(obj, i) {
                return obj[i];
            }
            filtered.sort(function (a, b) {
                var comparator;
                var reducedA = field.split('.').reduce(index, a);
                var reducedB = field.split('.').reduce(index, b);
                if (isNumeric(reducedA) && isNumeric(reducedB)) {
                    reducedA = Number(reducedA);
                    reducedB = Number(reducedB);
                }
                if (reducedA === reducedB) {
                    comparator = 0;
                } else {
                    comparator = reducedA > reducedB ? 1 : -1;
                }
                return comparator;
            });
            if (reverse) {
                filtered.reverse();
            }
            return filtered;
        };
    });

    angular.module('ng-inputdecimalseparator', [])
        .directive('inputDecimalSeparator', [
            '$locale',
            function ($locale, undefined) {
                return {
                    restrict: 'A',
                    require: '?ngModel',
                    compile: function (_element, tAttrs) {
                        return function (scope, element, attrs, ctrl, undefined) {
                            if (!ctrl) {
                                return;
                            }

                            var decimalDelimiter = $locale.NUMBER_FORMATS.DECIMAL_SEP,
                                thousandsDelimiter = "",
                                defaultDelimiter=".";
                            var decimalMax=isNaN(attrs.decimalMax)?null:parseFloat(attrs.decimalMax);
                            var decimalMin=isNaN(attrs.decimalMin)?null:parseFloat(attrs.decimalMin);
                            var noOfDecimal=null;

                            if (noOfDecimal || noOfDecimal != '')
                            {
                                noOfDecimal= isNaN(attrs.inputDecimalSeparator)?2:Number(attrs.inputDecimalSeparator);
                                noOfDecimal=Math.floor(noOfDecimal);
                            }

                            // Parser starts here...
                            ctrl.$parsers.push(function (value) {
                                if (!value || value === '') {
                                    return null;
                                }

                                var str = "[^0-9" + decimalDelimiter + "]";
                                var regularExpression = new RegExp(str, 'g');

                                var outputValue = value.replace(regularExpression, '');

                                var tokens = outputValue.split(decimalDelimiter);
                                tokens.splice(2, tokens.length - 2);

                                if (noOfDecimal && tokens[1])
                                    tokens[1] = tokens[1].substring(0, noOfDecimal);

                                var result = tokens.join(decimalDelimiter);
                                var actualNumber =tokens.join(defaultDelimiter);

                                ctrl.$setValidity('max', true);
                                ctrl.$setValidity('min', true);

                                if (decimalMax && Number(actualNumber) > decimalMax)
                                    ctrl.$setValidity('max', false);

                                if (decimalMin && Number(actualNumber) < decimalMin)
                                    ctrl.$setValidity('min', false);

                                // apply thousand separator
                                if (result) {
                                    tokens = result.split($locale.NUMBER_FORMATS.DECIMAL_SEP);
                                    if (tokens[0])
                                        tokens[0] = tokens[0].split( /(?=(?:...)*$)/ ).join("");

                                    result = tokens.join($locale.NUMBER_FORMATS.DECIMAL_SEP);
                                }

                                if (result != value) {
                                    ctrl.$setViewValue(result);
                                    ctrl.$render();
                                }

                                return actualNumber;

                            }); // end Parser

                            // Formatter starts here
                            ctrl.$formatters.push(function (value) {

                                if (!value || value === '') {
                                    return null;
                                }

                                value = value.toString();

                                var tokens = value.split(defaultDelimiter);
                                tokens.splice(2, tokens.length - 2);

                                if (noOfDecimal && tokens[1])
                                    tokens[1] = tokens[1].substring(0, noOfDecimal);

                                var result = tokens.join(decimalDelimiter);
                                var actualNumber =Number(tokens.join(defaultDelimiter));

                                if (decimalMax && actualNumber > decimalMax)
                                    ctrl.$setValidity('max', false);

                                if (decimalMin && actualNumber < decimalMin)
                                    ctrl.$setValidity('min', false);

                                // apply thousand separator
                                if (result) {
                                    tokens = result.split($locale.NUMBER_FORMATS.DECIMAL_SEP);
                                    if (tokens[0])
                                        tokens[0] = tokens[0].split( /(?=(?:...)*$)/ ).join("");

                                    result = tokens.join($locale.NUMBER_FORMATS.DECIMAL_SEP);
                                }
                                return result;
                            });
                        };  // end link function
                    } // end compile function
                };
            }
        ]);

    app.controller('HomeController', ['$scope', '$http', '$timeout', '$interval', '$sce', function($scope, $http, $timeout, $interval){
        $scope.loaded= true;
        $scope.markets= [];
        $scope.selectedMarket= "BTC";
        $scope.tickers= {};
        $scope.money = {
            usd: 0, cny: 0
        };
        $scope.marketFilter = "typeOrder";
        $scope.marketFilterDirection = false;

        $scope.login = function(){
            ajax_exit();
        };

        var socket;
        function connectWs() {
            socket && socket.close()
            socket = io(location.protocol + '//' + host + '/trade?deep=4&token=dev&symbol=' + $scope.selectedPair.fid, {transports: ['websocket', 'pull']});
            // ng fix
            var _on = socket.on
            socket.on = function(event, fn) {
                _on.call(socket, event, function(msg){
                    $scope.$apply(function(){
                        fn(msg)
                    })
                })
            }

            socket.on('entrust-buy', function (msg) {
                $scope.buyDepthList = eval(msg)
            });
            socket.on('entrust-sell', function (msg) {
                $scope.sellDepthList = eval(msg)
            });
            socket.on('entrust-log', function (msg) {
                $scope.recentDealList = eval(msg)
            });
            socket.on('real', function (msg) {
                var ticker = eval("(" + msg + ")")
                $scope.selectedPair.lastDealPrize = ticker.last
                $scope.selectedPair.volumn = ticker.vol
                //$scope.selectedPair.fupanddown = ticker.up * 100
            });
            socket.on('entrust-update', function (msg) {
                var newData = eval("(" + msg + ")");
                if ($scope.selectedPair.fid == newData.symbol) {
                    $scope.user.rmbfrozen = newData.rmbfrozen
                    $scope.user.rmbtotal = newData.rmbtotal
                    $scope.user.virtotal = newData.virtotal
                    $scope.user.virfrozen = newData.virfrozen
                    $scope.user.entrustList = newData.entrustList
                    $scope.user.entrustListLog = newData.entrustListLog
                } else {
                    $scope.user.rmbfrozen = newData.rmbfrozen
                    $scope.user.rmbtotal = newData.rmbtotal
                }
            });
        }

        function refreshUserInfo() {
            if ($scope.selectedPair) {
                $http.get(basePath + '/v2/market/refreshUserInfo?symbol=' + $scope.selectedPair.fid)
                    .then(function(res) {
                        $scope.user = res.data
                    })
            }
        }

        function marketRefresh() {
            if ($scope.selectedPair) {
                $http.get(basePath + '/v2/market/marketRefresh?deep=4&symbol=' + $scope.selectedPair.fid + '&t=' + new Date().getTime())
                    .then(function(res) {
                        $scope.buyDepthList = res.data.buyDepthList;
                        $scope.sellDepthList = res.data.sellDepthList;
                        $scope.recentDealList = res.data.recentDealList
                    })
            }
        }

        function loadFee() {
            if ($scope.selectedPair) {
                $http.get(basePath + '/v2/market/getFee?symbol=' + $scope.selectedPair.fid + '&t=' + new Date().getTime())
                    .then(function(res) {
                        $scope.buyFee = res.data.buyFee
                        $scope.sellFee = res.data.sellFee
                    })
            }
        }

        function autoRefreshUserInfo() {
            $interval(refreshUserInfo, 1000)
        }

        function autoMarketRefresh() {
            $interval(marketRefresh, 1000)
        }

        function checkLogin(type) {
            if (!$scope.user || $scope.user.isLogin != 1) {
                setErrorMessage(type, lang.signIn);
                return false
            }
            return true
        }

        function checkOrder(type) {
            var order = $scope[type + 'Order']
            if (order.amount < 0.00000001) {
                setErrorMessage(type, WARN_TEXT[-1])
                return false;
            }
            if (order.total < 0.0001) {
                setErrorMessage(type, WARN_TEXT[-111])
                return false;
            }
            return true
        }

        function setErrorMessage(type, msg) {
            if (type === 'buy') {
                $scope.buyErrorMessage = msg || ''
            } else {
                $scope.sellErrorMessage = msg || ''
            }
        }
        var coinList;

        function getETHValue(){
            $http.get(basePath + "/v2/market/getPrice").then(function(res){
                $scope.ethPrice = {usd: res.data.eth_usdt, cny: (res.data.eth_usdt * res.data.rate).toFixed(2)}
                $scope.btcPrice = {usd: res.data.btc_usdt, cny: (res.data.btc_usdt * res.data.rate).toFixed(2)}
                loadCoins();
            });
        }
        getETHValue();

        function loadCoins() {
            return $http.get(basePath + '/v2/market/coins')
                .then(function(res){
                    coinList = res.data.dataMap;
                    for(var k in coinList){
                        $scope.markets.push(k);
                    }
                    var param = location.hash.match(new RegExp(".*" + 'symbol' + "=([^\&]*)(\&?)","i"));
                    var symbol;
                    var right_ticker;
                    if(param && !isNaN(param[1])){
                        symbol = param[1];
                        for (var i = 0; i < $scope.markets.length; i++) {
                            for(var j = 0; j < coinList[$scope.markets[i]].length; j++){
                                var ticker = coinList[$scope.markets[i]][j];
                                if (ticker.fid == symbol) {
                                    right_ticker = ticker;
                                }
                            }
                        }
                    }
                    if(!right_ticker){
                        right_ticker = coinList[$scope.markets[0]][0];
                    }
                    $scope.money = $scope[right_ticker.group.toLowerCase() + "Price"];
                    getCoinList(right_ticker.group);
                    $scope.setPair(right_ticker)
                })
        }

        function getCoinList(market){
            $scope.selectedMarket = market;
            $scope.tickers = coinList[market];
        }

        $scope.setMarket = function(market){
            $scope.money = $scope[market.toLowerCase() + "Price"];
            getCoinList(market);
            $scope.setPair(coinList[market][0]);
        };

        var $target = $("#market_kline_sel").find("li[data-default=1]");
        $scope.setPair = function(ticker) {
            location.hash = 'symbol=' + ticker.fid;
            $scope.selectedPair = ticker;
            $scope.buyOrder = {price: '', amount: '', total: ''}
            $scope.sellOrder = {price: '', amount: '', total: ''}
            refreshUserInfo();
            marketRefresh();
            loadFee()
            connectWs()

            // $scope.buyOrder = {price: ticker.lowestSellPrize, amount: 0, total: 0}
            // $scope.sellOrder = {price: ticker.higestBuyPrize, amount: 0, total: 0}
            getTemplate($target.data("step"), $scope.selectedPair, function(){
                curText = $target.text();
                $target.addClass("cur").siblings().removeClass("cur");
            });
        };

        $scope.createOrder = function(type) {
            setErrorMessage(type, null)
            if (!checkLogin(type) || !checkOrder(type)) {
                return
            }

            var order = $scope[type + 'Order']
            var data = {
                symbol: $scope.selectedPair.fid,
                tradeAmount: order.amount,
                tradeCnyPrice: order.price,
                tradePwd: $scope.tradePassword
            };

            if (type === 'buy' && !$scope.buying) {
                $scope.buying = true
                $http.post(basePath + '/v2/market/buyBtcSubmit', $.param(data), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                    .then(function(res){
                        $scope.buying = false;
                        var msg = res.data.msg ? res.data.msg : "";
                        if (res.data.resultCode !== 0) {
                            setErrorMessage(type, WARN_TEXT[res.data.resultCode] + msg)
                        } else {
                            $scope.user.needTradePasswd = false;
                            $scope.buyOrder = {price: '', amount: '', total: ''}
                            // refreshUserInfo();
                        }
                    })
            } else if (type === 'sell' & !$scope.selling) {
                $scope.selling = true
                $http.post(basePath + '/v2/market/sellBtcSubmit', $.param(data), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                    .then(function(res){
                        $scope.selling = false;
                        var msg = res.data.msg ? res.data.msg : "";
                        if (res.data.resultCode !== 0) {
                            setErrorMessage(type, WARN_TEXT[res.data.resultCode] + msg)
                        } else {
                            $scope.user.needTradePasswd = false;
                            $scope.sellOrder = {price: '', amount: '', total: ''}
                            // refreshUserInfo();
                        }
                    })
            }
        }

        $scope.cancelOrder = function(order) {
            $http.post(basePath + '/v2/market/cancelEntrust', $.param({id: order[3]}), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .then()
        }

        $scope.setTotal = function(type) {
            var order  = $scope[type]
            var price = order.price || 0;
            var amount = order.amount || 0;

            if (amount !== 0) {
                order.total = (parseFloat(price) * parseFloat(amount)).toFixed(8).trimRight("0");
                if (order.total.slice(-1) == ".") {
                    order.total = order.total.trimRight(".")
                }
                $scope[type] = order
            }
        }

        $scope.setAmount = function(type) {
            var order  = $scope[type]
            var price = order.price || 0;
            var total = order.total || 0;
            var amount = parseFloat(total) / (parseFloat(price || 0));
            $scope[type].amount = isFinite(amount) ? amount.toString().exp() : 0;
        }

        $scope.setTotalOrAmount = function(type) {
            var order  = $scope[type]
            if (order.total && !order.amount) {
                $scope.setAmount(type);
            }
            $scope.setTotal(type);
        }

        $scope.setBuy = function(sell) {
            $scope.buyOrder.price = sell[0]
            $scope.buyOrder.amount = (sell[1] * 1).toFixed(8)
            $scope.setTotal('buyOrder')
        }
        $scope.setSell = function(buy) {
            $scope.sellOrder.price = buy[0]
            $scope.sellOrder.amount = (buy[1] * 1).toFixed(8)
            $scope.setTotal('sellOrder')
        }

        // autoRefreshUserInfo();
        //
        // autoMarketRefresh();

        // refreshUserInfo();
        //
        // marketRefresh();
        // loadTicker();

        /*$scope.$on("userChange", function () {
            $scope.$broadcast("userChangeFromParent", $scope.user);
        });*/
        var myChart = echarts.init(document.getElementById('chart')),curText = $target.text();
        $scope.getChart = function (e, step) {
            if($(e.target).text() == curText){
                return;
            }
            getTemplate(step, $scope.selectedPair, function(){
                curText = $(e.target).text();
                $(e.target).addClass("cur").siblings().removeClass("cur");
            });
        };

        var downColor = '#00da3c';
        var upColor = '#ec0000';

        function splitData(rawData, step) {
            var categoryData = [];
            var values = [];
            var volumes = [];
            for (var i = 0; i < rawData.length; i++) {
                var time = new Date(rawData[i].splice(0, 1)[0] * 1000).format(getTimeFormat(step));
                categoryData.push(time);
                rawData[i].splice(0,2);//删除多余两个0
                values.push(rawData[i]);
                volumes.push([i, rawData[i][4], rawData[i][0] > rawData[i][1] ? 1 : -1]);
                rawData[i].push(time);
            }
            return {
                categoryData: categoryData,
                values: values,
                volumes: volumes
            };
        }

        function calculateMA(dayCount, data) {
            var result = [];
            for (var i = 0, len = data.values.length; i < len; i++) {
                if (i < dayCount) {
                    result.push('-');
                    continue;
                }
                var sum = 0;
                for (var j = 0; j < dayCount; j++) {
                    sum += data.values[i - j][1];
                }
                result.push(+(sum / dayCount).toFixed(3));
            }
            return result;
        }
        function getTimeFormat(step){
            if(step >= 60 && step < 60 * 60){
                return "yy-MM-dd HH:mm";
            }else if(step >= 60 * 60 && step < 60 * 60 * 24){
                return "yy-MM-dd HH";
            }else if(step >= 60 * 60 * 24){
                return "yy-MM-dd";
            }else{
                return "yy-MM-dd HH:mm:ss";
            }
        }

        function getTemplate(step, ticker, callback){
            $.getJSON('/v2/market/period?step='+step+'&symbol='+ticker.fid, function (rawData) {
                callback && callback();
                var data = splitData(rawData, step);
                myChart.setOption({
                    backgroundColor: '#fff',
                    animation: false,
                    legend: {
                        bottom: 0,
                        left: 'center',
                        data: ['OHLC', 'MA5', 'MA10', 'MA20', 'MA30']
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross'
                        },
                        backgroundColor: 'rgba(245, 245, 245, 0.8)',
                        borderWidth: 1,
                        borderColor: '#ccc',
                        padding: 10,
                        textStyle: {
                            color: '#000',
                            fontSize: 12
                        },
                        position: function (pos, params, el, elRect, size) {
                            var obj = {top: 10};
                            obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 50;
                            return obj;
                        },
                        extraCssText: 'width: auto'
                    },
                    axisPointer: {
                        link: {xAxisIndex: 'all'},
                        label: {
                            backgroundColor: '#777'
                        }
                    },
                    toolbox: {
                        feature: {
                            dataZoom: {
                                show: false
                            },
                            brush: {
                                type:['rect','polygon','lineX','lineY','keep','clear'],
                                title: {
                                    rect: 'Rectangular Selection',
                                    polygon: 'Circle Selection',
                                    lineX: 'Horizontal Selection',
                                    lineY: 'Vertical Selection',
                                    keep: 'Keep',
                                    clear: 'Clear'
                                }
                            }
                        }
                    },
                    brush: {
                        xAxisIndex: 'all',
                        brushLink: 'all',
                        outOfBrush: {
                            colorAlpha: 0.1
                        }
                    },
                    visualMap: {
                        show: false,
                        seriesIndex: 5,
                        dimension: 2,
                        pieces: [{
                            value: 1,
                            color: downColor
                        }, {
                            value: -1,
                            color: upColor
                        }]
                    },
                    grid: [
                        {
                            left: '5%',
                            right: '1%',
                            height: '45%',
                            top:'11%'
                        },
                        {
                            left: '5%',
                            right: '1%',
                            top: '65%',
                            height: '16%'
                        }
                    ],
                    xAxis: [
                        {
                            type: 'category',
                            data: data.categoryData,
                            scale: true,
                            boundaryGap : false,
                            axisLine: {onZero: false},
                            splitLine: {show: false},
                            splitNumber: 20,
                            min: 'dataMin',
                            max: 'dataMax'
                        },
                        {
                            type: 'category',
                            gridIndex: 1,
                            data: data.categoryData,
                            scale: true,
                            boundaryGap : false,
                            axisLine: {onZero: false},
                            axisTick: {show: false},
                            splitLine: {show: false},
                            axisLabel: {show: false},
                            splitNumber: 20,
                            min: 'dataMin',
                            max: 'dataMax',
                            axisPointer: {
                                label: {
                                    formatter: function (params) {
                                        var seriesValue = (params.seriesData[0] || {}).value;
                                        return params.value
                                            + (seriesValue != null
                                                    ? '\n' + echarts.format.addCommas(seriesValue)
                                                    : ''
                                            );
                                    }
                                }
                            }

                        }
                    ],
                    yAxis: [
                        {
                            scale: true,
                            splitArea: {
                                show: true
                            }
                        },
                        {
                            scale: true,
                            gridIndex: 1,
                            splitNumber: 2,
                            axisLabel: {show: false},
                            axisLine: {show: false},
                            axisTick: {show: false},
                            splitLine: {show: false}
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'inside',
                            xAxisIndex: [0, 1],
                            start: 0,
                            end: 100
                        },
                        {
                            show: true,
                            xAxisIndex: [0, 1],
                            type: 'slider',
                            top: '85%',
                            start: 0,
                            end: 100
                        }
                    ],
                    series: [
                        {
                            name: 'OHLC',
                            type: 'candlestick',
                            data: data.values,
                            itemStyle: {
                                normal: {
                                    color: upColor,
                                    color0: downColor,
                                    borderColor: null,
                                    borderColor0: null
                                }
                            },
                            barMaxWidth:10
                        },
                        {
                            name: 'MA5',
                            type: 'line',
                            data: calculateMA(5, data),
                            smooth: true,
                            lineStyle: {
                                normal: {opacity: 0.5}
                            }
                        },
                        {
                            name: 'MA10',
                            type: 'line',
                            data: calculateMA(10, data),
                            smooth: true,
                            lineStyle: {
                                normal: {opacity: 0.5}
                            }
                        },
                        {
                            name: 'MA20',
                            type: 'line',
                            data: calculateMA(20, data),
                            smooth: true,
                            lineStyle: {
                                normal: {opacity: 0.5}
                            }
                        },
                        {
                            name: 'MA30',
                            type: 'line',
                            data: calculateMA(30, data),
                            smooth: true,
                            lineStyle: {
                                normal: {opacity: 0.5}
                            }
                        },
                        {
                            name: 'Volume',
                            type: 'bar',
                            xAxisIndex: 1,
                            yAxisIndex: 1,
                            data: data.volumes,
                            barMaxWidth:10
                        }
                    ]
                }, true);
               /* myChart.dispatchAction({
                    type: 'brush',
                    areas: [
                        {
                            brushType: 'lineX',
                            coordRange: [rawData[Math.ceil(rawData.length/2)][5], ''+rawData[rawData.length-1][5]],
                            xAxisIndex: 0
                        }
                    ]
                });*/
            });
        }
    }]);

    app.controller('ChatController', ['$scope', '$http', '$timeout', '$interval', '$sce', function($scope, $http, $timeout){
        $scope.chat_messages = [];

       /* $scope.$on("userChangeFromParent", function (event, msg) {
            $scope.user = msg;
        });*/

        function checkUser() {
            if (!$scope.user || $scope.user.isLogin != 1) {
                return false
            }
            return true
        }

        $scope.initChat = function() {
            $http.get(basePath + '/v2/market/chat_messages').then(function($res) {
                var res = $res.data;
                if(res.code == 0){
                    $scope.chat_messages = res.data;
                    var objDiv = document.getElementById("chat-container");
                    $timeout(function(){
                        objDiv.scrollTop = objDiv.scrollHeight - objDiv.offsetHeight;
                    },300)
                }
            });
        };
        $scope.initChat();
        $scope.sendChat = function(){
            if(!checkUser()){
                error_dialog($scope.lang.signIn,function(){
                    ajax_exit();
                });
                return;
            }
            var mes = $scope.chat.message;
            if(isEmpty(mes)){
                error_dialog(lang.exchange.chat_empty_tips);
                return;
            }
            if(mes.length > 100){
                error_dialog(lang.exchange.chat_larger_tips);
                return;
            }
            $http.post(basePath + "/v2/market/push_chat_message",null,{params: {message: mes}}).then(function($res){
                var res = $res.data;
                if(res.code == 101){
                    error_dialog(lang.exchange.chat_empty_tips);
                }else if(res.code == 103){
                    error_dialog(lang.exchange.chat_close_tips);
                }else if(res.code == 105){
                    error_dialog(lang.exchange.chat_close_tips);
                }else{
                    $scope.chat.message = "";
                }
            });
        };

        function shiftAndPush(arr, num, obj){
            var length = arr.length;
            if(length >= num){
                arr.shift();
            }
            arr.push(obj);
            return arr;
        }

        var chat_socket;
        function connectChatWs() {
            chat_socket && chat_socket.close();
            chat_socket = io(location.protocol + '//' + host + '/market_chat', {transports: ['websocket', 'pull']});
            var _on = chat_socket.on;
            chat_socket.on = function(event, fn) {
                _on.call(chat_socket, event, function(msg){
                    $scope.$apply(function(){
                        fn(msg)
                    })
                })
            };
            chat_socket.on('chat-message', function (msg) {
                var chat = JSON.parse(msg);
                shiftAndPush($scope.chat_messages, 100, chat);
                var objDiv = document.getElementById("chat-container");
                if(Math.abs(objDiv.scrollTop + objDiv.offsetHeight - objDiv.scrollHeight) < 10 || (checkUser() && chat.userId ==  $scope.user.userId)){
                    $timeout(function(){
                        objDiv.scrollTop = objDiv.scrollHeight - objDiv.offsetHeight;
                    },300)
                }
            });
        }
        $timeout(connectChatWs, 200)
        // connectChatWs();
    }]);
})();