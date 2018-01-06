$(function () {
    var order_socket; //全局socket
    var host = "127.0.0.1:9092";
    var marketId = $("#symbol").val();
    //界面第一次后台获取挂单信息
    getFentrusts(marketId);

    //链接socket
    function connectWs() {
        order_socket && order_socket.close();
        order_socket = io(location.protocol + '//' + host + '/trade?deep=4&token=dev&symbol=' + marketId, {transports: ['websocket', 'pull']});

        order_socket.on('entrust-buy', function (msg) {
            var buy = JSON.parse(msg);
            var buy_html = "";
            for (var i = 0; i < buy.length; i++) {
                buy_html += "<dd name= 'depth-item' data-info= " +buy[i][0].toFixed(4) +"><div class='inner'><span class='title color-buy'>"
                    +"买" + "</span> <span class='price'>"+buy[i][0].toFixed(4)+"</span>"
                    +"<span class='amount'>" +buy[i][1].toFixed(4)+ "</span><span>" +buy[i][2].toFixed(4) +"</span></div></dd>";
            }
            $("#buy_fentrust").html(buy_html);
        });
        order_socket.on('entrust-sell', function (msg) {
            var sell = JSON.parse(msg);
            var sell_html = "";
            for (var i = 0; i < sell.length; i++) {
                sell_html += "<dd name= 'depth-item' data-info= " +sell[i][0].toFixed(4) +"><div class='inner'><span class='title color-sell'>"
                    +"卖" + "</span> <span class='price'>"+sell[i][0].toFixed(4)+"</span>"
                    +"<span class='amount'>" +sell[i][1].toFixed(4)+ "</span><span>" +sell[i][2].toFixed(4) +"</span></div></dd>";
            }
            $("#sell_fentrust").html(sell_html);
        });
        order_socket.on('entrust-log', function (msg) {
        });
        order_socket.on('real', function (msg) {
            // var ticker = eval("(" + msg + ")")
            // $scope.selectedPair.lastDealPrize = ticker.last
            // $scope.selectedPair.volumn = ticker.vol
        });
        order_socket.on('entrust-update', function (msg) {
            var newData = eval("(" + msg + ")");
            // if ($scope.selectedPair.fid == newData.symbol) {
            //     $scope.user.rmbfrozen = newData.rmbfrozen
            //     $scope.user.rmbtotal = newData.rmbtotal
            //     $scope.user.virtotal = newData.virtotal
            //     $scope.user.virfrozen = newData.virfrozen
            //     $scope.user.entrustList = newData.entrustList
            //     $scope.user.entrustListLog = newData.entrustListLog
            // } else {
            //     $scope.user.rmbfrozen = newData.rmbfrozen
            //     $scope.user.rmbtotal = newData.rmbtotal
            // }
        });
    }
    setTimeout(connectWs, 200);
});

function getFentrusts(symbol) {
    $.get("/market/depth.html",{symbol:symbol},function (data) {
        data = JSON.parse(data);
        var buy = data.return.bids;
        var sell = data.return.asks;
        var buy_html = "";
        var sell_html = "";
        for (var i = 0; i < buy.length; i++) {
            buy_html += "<dd name= 'depth-item' data-info= " +buy[i][0].toFixed(4) +"><div class='inner'><span class='title color-buy'>"
            +"买" + "</span> <span class='price'>"+buy[i][0].toFixed(4)+"</span>"
            +"<span class='amount'>" +buy[i][1].toFixed(4)+ "</span><span>" +buy[i][2].toFixed(4) +"</span></div></dd>";
        }
        for (var i = 0; i < sell.length; i++) {

            sell_html += "<dd name= 'depth-item' data-info= " +sell[i][0].toFixed(4) +"><div class='inner'><span class='title color-sell'>"
                +"卖" + "</span> <span class='price'>"+sell[i][0].toFixed(4)+"</span>"
                +"<span class='amount'>" +sell[i][1].toFixed(4)+ "</span><span>" +sell[i][2].toFixed(4) +"</span></div></dd>";
        }
        $("#sell_fentrust").html(sell_html);
        $("#buy_fentrust").html(buy_html);
    })
}