<%--
  Created by IntelliJ IDEA.
  User: sunpeng
  Date: 2017/12/29
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="${resources}/static/css/chat.css"/>
<div class="chat-div" id="chatRoom" style="display: none">
    <div style="    margin-left: 43%;color: #0AD5C1;font-size: 17px;" >聊天室
        <span style="margin-left: 150px;cursor: pointer;color: red;" onclick="closeRoom()">关闭</span>
    </div>
    <ul class="chat-thread" id="content">
        <%--<li>Are we meeting today?</li>--%>
        <%--<li>yes, what time suits you?</li>--%>
        <%--<li>I was thinking after lunch, I have a meeting in the morning</li>--%>
        <%--<li>Are we meeting today?</li>--%>
        <%--<li>yes, what time suits you?</li>--%>
        <%--<li>I was thinking after lunch, I have a meeting in the morning</li>--%>
        <%--<li>Are we meeting today?</li>--%>
        <%--<li>yes, what time suits you?</li>--%>
        <%--<li>I was thinking after lunch, I have a meeting in the morning</li>--%>
        <%--<li>Are we meeting today?</li>--%>
        <%--<li>yes, what time suits you?</li>--%>
        <%--<li>I was thinking after lunch, I have a meeting in the morning</li>--%>
    </ul>
    <div style="height: 8%">
        <textarea  id="chatContent" class="chat_conten_style" ></textarea>
        <div class="send_style" onclick="send()">发送</div>
    </div>
</div>

<%--<div  id="showRoom"  style="position: fixed;bottom: 5px;right: 2px;width: 75px;height: 30px;background-color: #269abc;">--%>
    <%--聊天室--%>
<%--</div>--%>

<%--<button id="showRoom"  style="position: fixed;bottom: 5px;right: 2px;width: 75px;height: 30px;background-color: #269abc;">聊天室</button>--%>

<script>
    //页面一加载
    $(function() {
        $("#showRoom").click(function () {
            $("#chatRoom").show();
            $('#content').scrollTop($('#content')[0].scrollHeight + 30);
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
        $.get("http://118.190.132.141:1000/getMessage",function (data) {
            var html = "";
            for (var i = 0; i < data.length; i++) {

                html += "<li>" +data[i].name+": " +data[i].message + "</li>";
            }
            $("#content").html(html);
            $('#content').scrollTop( $('#content')[0].scrollHeight + 30 );
        });

    }

    var websocket = null;
    function connect() {
        //判断当前浏览器是否支持WebSocket
        if('WebSocket' in window){
            websocket = new WebSocket("ws://118.190.132.141:1000/websocket");
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
        var html ="";
        var $loginedBar = $('#userInfoBar');
        var id = $loginedBar.find('span[name="userId"]').text();
//        if(id.toString() == message.toString().split(":")[1]){
//            html =  "<li class='your'>" + message + "</li>"
//        }else {
//            html =  "<li class='other'>" + message + "</li>"
//        }

        html =  "<li>" + message + "</li>"
        document.getElementById('content').innerHTML= document.getElementById('content').innerHTML +html ;
        $('#content').scrollTop( $('#content')[0].scrollHeight + 30 );
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