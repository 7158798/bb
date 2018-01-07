var app =  angular.module('myapp',[]);
app.config(['$locationProvider', function($locationProvider) {
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]);
app.controller("newtradeController",['$scope', '$http','$location','$timeout',function($scope,$http,$location,$timeout){
    $scope.marketId = $location.search().symbol;
    //推荐买一价   卖一价
    $scope.sellPrice = 0;
    $scope.buyPrice = 0;

    $scope.buyAmount = 0; //交易总额
    $scope.sellAmount = 0;
    $scope.buyCount =0;
    $scope.sellCount =0;

    function getUserInfo() {
        $http.get("/market/refreshUserInfo?symbol="+$scope.marketId)
            .then(function (result) {
                $scope.user = result.data;
                console.log($scope.user);
                $scope.buyPrice = $scope.user.recommendPrizebuy;
                $scope.sellPrice = $scope.user.recommendPrizesell;
                if($scope.user.isLogin === 1){
                    getUserFentrust($scope.marketId);
                }
            })
    }
    getUserInfo();

    $scope.buyOrders =[];
    $scope.sellOrders =[];

    //获取所有挂单
    function getFentrusts(symbol) {
        $http.get("/market/marketRefresh?symbol="+symbol)
            .then(function (result) {
                $scope.buyOrders = result.data.buyDepthList;
                $scope.sellOrders = result.data.sellDepthList;
                $scope.recentDealList = result.data.recentDealList;
            })
    };
    // getFentrusts($scope.marketId);

    //点击挂单事件
    $scope.clickOrder = function(order){
        $scope.sellPrice = order[0];
        $scope.buyPrice = order[0];
        $scope.buyAmount = order[0] * $scope.buyCount;
        $scope.sellAmount = order[0] * $scope.sellCount;
    };
    //数量改变事件
    $scope.countChange = function(flag){
        if(flag === 0){ //买
            $scope.buyAmount = $scope.buyPrice * $scope.buyCount;
        }else {
            $scope.sellAmount = $scope.sellPrice * $scope.sellCount;
        }
    };

     var order_socket; //全局socket
    //链接socket
    function connectWs() {
        var host = "127.0.0.1:9092";
        order_socket && order_socket.close();
        order_socket = io(location.protocol + '//' + host + '/trade?deep=4&token=dev&symbol=' + $scope.marketId, {transports: ['websocket', 'pull']});
        var _on = order_socket.on;
        order_socket.on = function(event, fn) {
            _on.call(order_socket, event, function(msg){
                $scope.$apply(function(){
                    fn(msg)
                })
            })
        };
        order_socket.on('entrust-buy', function (msg) {
            $scope.buyOrders = eval(msg);
        });
        order_socket.on('entrust-sell', function (msg) {
            $scope.sellOrders = JSON.parse(msg);
        });
        order_socket.on('entrust-log', function (msg) {
            $scope.recentDealList = JSON.parse(msg);
        });
        order_socket.on('real', function (msg) {
            // var ticker = eval("(" + msg + ")")
            // $scope.selectedPair.lastDealPrize = ticker.last
            // $scope.selectedPair.volumn = ticker.vol
        });
        order_socket.on('entrust-update', function (msg) {
            var newData = eval("(" + msg + ")");
            if ($scope.marketId === newData.symbol) {
                $scope.user.rmbfrozen = newData.rmbfrozen;
                $scope.user.rmbtotal = newData.rmbtotal;
                $scope.user.virtotal = newData.virtotal;
                $scope.user.virfrozen = newData.virfrozen;
                $scope.user.entrustList = newData.entrustList;
                $scope.user.entrustListLog = newData.entrustListLog;
            } else {
                $scope.user.rmbfrozen = newData.rmbfrozen;
                $scope.user.rmbtotal = newData.rmbtotal;
            }
        });
    }
    connectWs();

    $scope.showTip = false;
    var WARN_TEXT = {
        '-1': "最小交易数量为：",
        '-2': "密码错误",
        '-3': "交易金额最低为：",
        '-4': "余额不足",
        // language=HTML
        '-5': "未设置交易密码，<a class='c_blue' href='/profile#security'> 去设置</a>",
        '-6': "您的价格超出了最新成交价，请检查您的输入",
        '-8': "输入您的交易密码",
        '-100': "币种不存在",
        '-101': "交易已暂停",
        '-400': "不在交易时间内",
        '-50': "交易密码不能为空",
        '-200': "网络错误",
        '-900': "交易价格超出限制",
        '-111': "最小交易额不能小于0.001",
        '-112': "交易金额太小",
        '-113': "交易数量不能小于0.0001",
        '-35': "交易金额最高为：",
        '-19': "请当心，交易价格和推荐价格出入过大"
    };

    $scope.createOrder = function(type) {
        $scope.showTip = true;
        if (!checkLogin(type) || !checkOrder(type)) {
            return;
        }
        if (type === 'buy') {
            var data = {
                symbol: $scope.marketId,
                tradeAmount: $scope.buyCount,
                tradeCnyPrice: $scope.buyPrice,
                tradePwd: $scope.tradePassword
            };
            $http.post('/market/buyBtcSubmit', $.param(data), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .then(function(res){
                    var msg = res.data.msg ? res.data.msg : "";
                    if (res.data.resultCode !== 0) {
                        setErrorMessage(type, WARN_TEXT[res.data.resultCode] + msg)
                    } else {
                        $scope.buyCount = 0;
                        $scope.buyPrice = 0;
                        $scope.user.needTradePasswd = false;
                        setErrorMessage(type, "购买成功");
                    }
                    $timeout(function () {
                        $scope.showTip = false;
                    },1000);
                })
        } else {
            var data = {
                symbol: $scope.marketId,
                tradeAmount: $scope.sellCount,
                tradeCnyPrice: $scope.sellPrice,
                tradePwd: $scope.tradePassword
            };
            $http.post('/market/sellBtcSubmit', $.param(data), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .then(function(res){
                    $scope.showTip = true;
                    var msg = res.data.msg ? res.data.msg : "";
                    if (res.data.resultCode !== 0) {
                        setErrorMessage(type, WARN_TEXT[res.data.resultCode] + msg)
                    } else {
                        $scope.sellCount = 0;
                        $scope.sellPrice = 0;
                        $scope.user.needTradePasswd = false;
                        setErrorMessage(type, "出售成功");
                    }
                    $timeout(function () {
                        $scope.showTip = false;
                    },1000);
                })
        }

    };

    $scope.cancelOrder = function(id) {
        $http.post('/market/cancelEntrust', $.param({id: id}), {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .then()
    };

    function setErrorMessage(type, msg) {
        if (type === 'buy') {
            $scope.buyErrorMessage = msg ;
        } else {
            $scope.sellErrorMessage = msg;
        }
    }
    function checkLogin(type) {
        if (!$scope.user || $scope.user.isLogin !== 1) {

            setErrorMessage(type, "请登录");
            return false;
        }
        return true
    }
    function checkOrder(type) {
        if(type === "sell"){
            if ($scope.sellCount < 0.0001) {
                setErrorMessage(type, "最小数量为0.0001");
                return false;
            }
            if ($scope.sellAmount< 0.01) {
                setErrorMessage(type, "最小金额为0.01");
                return false;
            }
        }else {
            if ($scope.buyCount < 0.0001) {
                setErrorMessage(type, "最小数量为0.0001");
                return false;
            }
            if ($scope.buyAmount < 0.01) {
                setErrorMessage(type, "最小金额为0.01");
                return false;
            }
        }
        return true
    }

    //获取用户订单记录
    function getUserFentrust(symbol) {
        $http.get("/market/userFentrust?symbol="+symbol)
            .then(function (result) {
                $scope.userOrders = result.data.entrustList;
                $scope.userOrderLogs = result.data.entrustListLog;
            })
    }


}]);