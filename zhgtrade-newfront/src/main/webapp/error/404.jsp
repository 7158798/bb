<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>404</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit'/>
    <meta name="keywords" content="虚拟币,数字货币,股权众筹,科技众筹,招股科技,众股交易所">
    <meta name="description" content="金融服务生活，智能改变世界。众股，致力于实现数字货币与科技实体的结合；依托招股科技实体公司，将以身作则，破除郁金香式的炒作，共建数字货币新蓝图。">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <style>
        *{padding: 0;margin: 0;}
        div{text-align: center;margin: 300px auto;width: 362px;}

        a{text-decoration: none;}
        ul{display: block;margin: 10px auto;width: 150px;float: left;}
        ul li{float: left;font-size: 12px;list-style: none;}
    </style>
</head>
<body>
    <div>
        <span style="font-size: 30px;">额，页面弄丢了！</span>
        <span id="title" style="font-size: 12px;color: #969696;">5秒后自动跳转到首页</span>
        <ul>
            <li><a href="/">返回首页</a></li>
            <li style="margin-left: 20px;"><a href="javascript:window.history.back();">返回上一页</a></li>
        </ul>
    </div>
<script>
    for(var i=1; i<=5; i++){
        setTimeout('setTip(' + i + ')', i * 1000);
    }
    function setTip(i){
        if(5 == i){
            window.location.href = "/";
            return;
        }
        document.getElementById('title').innerHTML = (5 - i) + '秒后自动跳转到首页';
    }
</script>
</body>
</html>