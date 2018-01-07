
<%--
  Created by IntelliJ IDEA.
  User: sunpeng
  Date: 2017/12/29
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"  %>
<link rel="stylesheet" href="${resources}/static/css/chat.css"/>
<div class="chat-div" id="chatRoom" style="display: none">
    <div style="    margin-left: 0%;color: rgba(254, 116, 31, 0.9);font-size: 17px;" >
        <span>聊天室</span>
        <span style="float:right;margin-right: 10px;cursor: pointer;color: rgba(254, 116, 31, 0.9);" onclick="closeRoom()">关闭</span>
    </div>
    <ul class="chat-thread" id="content11">
    </ul>
    <div style="height: 27px;width: 103%">
        <input type="text" id="chatContent" class="chat_conten_style"/>
        <div class="send_style" onclick="send()">
            <div style="color: white;font-size: 16px;margin-left: 10px;margin-top: 5px" >发送</div>
        </div>
    </div>
</div>

<script>
    //页面一加载
    $(function() {
        $("#showRoom").click(function () {
            $("#chatRoom").show();
            $("#chatContent").focus();
            $('#content11').scrollTop($('#content11')[0].scrollHeight + 30);
        });
        connect();

        $(document).keydown(function (event) {
            if (event.keyCode == 13) {
                send();
            }
        });
    });

    function closeRoom() {
        $("#chatRoom").hide();
    }
    function initMessage() {
//        $.get("http://118.190.132.141:1000/getMessage",function (data) {
        $.get("http://60.205.211.129:1000/getMessage",function (data) {
            var html = "";
            for (var i = 0; i < data.length; i++) {

                html += "<li> <div class='name'>" +data[i].date.toString().substring(11,19) +" " +data[i].name+ "</div> <div class='message'>" +data[i].message + "</div></li>";
            }
            $("#content11").html(html);
            $('#content11').scrollTop( $('#content11')[0].scrollHeight + 30 );
        });

    }

    var websocket = null;
    function connect() {
        //判断当前浏览器是否支持WebSocket
        if('WebSocket' in window){
//            websocket = new WebSocket("ws://118.190.132.141:1000/websocket");
            websocket = new WebSocket("ws://60.205.211.129:1000/websocket");
        }
        else{
            alert('Not support websocket')
        }

        //连接发生错误的回调方法
        websocket.onerror = function(){
//            setMessageInnerHTML("连接失败");
        };

        //连接成功建立的回调方法
        websocket.onopen = function(event){
            initMessage();
        }
        //接收到消息的回调方法
        websocket.onmessage = function(event){
            setMessageInnerHTML(event.data);
        };

        //连接关闭的回调方法
        websocket.onclose = function(){
//            setMessageInnerHTML("连接关闭");
        };

        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function(){
            websocket.close();
        }
    }

    //将消息显示在网页上
    function setMessageInnerHTML(message){
        var html;
        var mes = message.toString().split("$");
        html =  "<li> <div class='name'>"+mes[2] +"  "+mes[0] +"</div> <div class='message'>" +mes[1] + "</div></li>"
        document.getElementById('content11').innerHTML= document.getElementById('content11').innerHTML +html ;
        $('#content11').scrollTop( $('#content11')[0].scrollHeight + 30 );
    }

    //发送消息
    function send(){
        var $loginedBar = $('#userInfoBar');
        var id = $loginedBar.find('span[name="userId"]').text();
        console.log(id)
        var message = document.getElementById('chatContent').value;
        if(message == null || message.toString().trim()==0){
            return;
        }
        if(id!="" && id !=null){
            message = message +"##" + id.split(":")[1];
        }else {
            message = message +"##" + "-1";
        }
        websocket.send(message);
        $("#chatContent").val("");
    }
</script>