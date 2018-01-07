package com.lc.push.core;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.lc.push.data.Constants;
import com.lc.push.data.RealDataService;
import com.lc.push.mq.MessageListener;
import com.lc.push.utils.HttpUtils;
import com.lc.push.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.Closeable;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class PushServer implements MessageListener<String>, ConnectListener, DisconnectListener, Closeable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private boolean serverStarted;

    private boolean messageCenterStarted;

    @Resource
    private SocketIOServer server;

    @Resource
    private MessageCenter messageCenter;

    @Resource
    private RealDataService dataService;

    @Resource
    private SessionManager sessionManager;

    @Resource
    private ExecutorService executorService;

    private String siteUrl;

    private int delay;

    public void start() {
        log.debug("start push server");
        startListeners();
        server.start();
        serverStarted = true;
        messageCenter.start(this);
        messageCenterStarted = true;
    }

    private void startListeners() {
        // 交易实时行情
        SocketIONamespace tradeSocketIONamespace = server.addNamespace("/trade");
        tradeSocketIONamespace.addConnectListener(this);
        tradeSocketIONamespace.addDisconnectListener(this);

        SocketIONamespace chatSocketIONamespace = server.addNamespace("/market_chat");
        chatSocketIONamespace.addConnectListener(this);
        chatSocketIONamespace.addDisconnectListener(this);
    }

    private void joinTradeRoom(SocketIOClient client) {
        String symbol = client.getHandshakeData().getSingleUrlParam("symbol");
        String deep = client.getHandshakeData().getSingleUrlParam("deep");
        client.sendEvent("ok"); // 连接成功后发送一条消息，可以加快客户端连接速度
        if(StringUtils.hasText(symbol)){
            String room = "/trade-" + symbol + "-" + deep;
            client.joinRoom(room);
            sendAllMessage(client, symbol, deep);       // 连接的时候，发送所有最新消息到客户端，以免出现连接速度太慢，而造成丢失的现象
        }else{
            log.error("无效币ID", symbol);
        }
    }

    private void sendAllMessage(SocketIOClient client, String symbol, String deep) {
        sendDepthEntrustToClient(client, symbol + ":0:" + deep);
        sendDepthEntrustToClient(client, symbol + ":1:" + deep);
        sendEntrustLogToClient(client, symbol);
        sendRealPriceToClient(client, symbol);
//        sendUserInfo(client, symbol);
    }

    private void sendUserInfo(SocketIOClient client, String symbol) {
        String content = getUserInfoFromWebServer(Integer.parseInt(symbol), sessionManager.getHttpSessionId(client));
        client.sendEvent("entrust-update", content);
    }

    private void sendDepthEntrustToClient(SocketIOClient client, String channel) {
        String[] params = channel.split(":");
        int type = Integer.valueOf(params[1]);
        String etype = type == 0 ? "entrust-buy" : "entrust-sell";
        log.trace("push {} data to client {}.", etype, client.getSessionId());
        client.sendEvent(etype, dataService.getDepthEntrust(channel));
    }

    private void sendEntrustLogToClient(SocketIOClient client, String symbol) {
        // 推送成交日志
        int fviFid = Integer.valueOf(symbol);
        String entrustLog = dataService.getEntrustLog(fviFid);
        log.trace("push entrust-log data to client {}.", client.getSessionId());
        client.sendEvent("entrust-log", entrustLog);
    }

    private void sendRealPriceToClient(SocketIOClient client, String symbol) {
        // 推送实时价格
        int fviFid = Integer.valueOf(symbol);
        String realPrice = getTickerFromWebServer(fviFid);
        log.trace("push real data to client {}.", client.getSessionId());
        client.sendEvent("real", realPrice);
    }

    private void joinChatRoom(SocketIOClient client) {
        client.sendEvent("ok"); // 连接成功后发送一条消息，可以加快客户端连接速度
        client.joinRoom(Constants.MARKET_CHAT_ROOM);
    }

    @Override
    public void onConnect(SocketIOClient client) {
        sessionManager.addSession(client);
        String namespace = client.getNamespace().getName();
        switch (namespace) {
            case "/trade":
                // 交易房间
                joinTradeRoom(client);
                break;
            case "/market_chat":
                // 聊天室
                joinChatRoom(client);
                break;
            default:
                log.error("not found namespace {}", namespace);
        }
    }

    @Override
    public void onDisconnect(SocketIOClient client) {
        // 客户端断开，发送当前在线人数 - 1
//        String room = client.getNamespace().getName();
//        server.getRoomOperations(room).sendEvent("lease", server.getAllClients().size() - 1);
        sessionManager.removeSession(client);
    }

    @Override
    public void onMessage(String channel, String message) {
        executorService.execute(() -> {
//            log.debug("onMessage {}, {}", channel, message);
            if (channel.startsWith("cache:fentrustlog")) {
                pushEntrustLog(channel);
            } else if (channel.startsWith("cache:latest")) {
                pushRealPrice(channel);
            } else if (channel.startsWith("user:entrust")) {
                pushUserEntrustData(channel, message);
            } else if(channel.startsWith(Constants.MARKET_CHAT_CHANNEL)){
                pushChatMessage(channel, message);
            }else {
                pushEntrustData(channel, message);
            }
        });
    }

    private void pushChatMessage(String channel, String message) {
        log.trace("push message data to room {}.", Constants.MARKET_CHAT_ROOM);
        server.getRoomOperations(Constants.MARKET_CHAT_ROOM).sendEvent("chat-message", message);
    }

    private void pushUserEntrustData(String channel, String message) {
        try {

            int fviFid = Integer.valueOf(channel.split(":")[2]);
            int userId = Integer.valueOf(message);

            // 单个用户，多端登录，需要全部推送
            Set<SocketIOClient> userSessions = sessionManager.getUserSessions(userId);
            if (userSessions != null && userSessions.size() > 0) {
                log.trace("push entrust-update data to user {}.", userId);
                // 这里的推送逻辑改了一下
                // 原来是更新所有频道，现在只推送当前币的频道，其他频道定时更新就好了，不然判断有点多，查询的数据也多，影响效率
                userSessions.forEach(client -> {
                    int symbol = Integer.valueOf(client.getHandshakeData().getSingleUrlParam("symbol"));
                    if (fviFid == symbol) {
                        String sid = sessionManager.getHttpSessionId(client);
                        String content = getUserInfoFromWebServer(symbol, sid);
                        client.sendEvent("entrust-update", content);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("pushUserEntrustData error reason " + e.getLocalizedMessage());
        }
    }

    private String getUserInfoFromWebServer(int fviFid, String sid) {
        return HttpUtils.get(siteUrl + "/market/refreshUserInfo?symbol=" + fviFid, sid);
    }

    private String getTickerFromWebServer(int fviFid) {
        return HttpUtils.get(siteUrl + "/market/real?symbol=" + fviFid, null);
    }

    private void pushRealPrice(String channel) {
        // 推送实时价格
        String[] params = channel.split(":");
        int fviFid = Integer.valueOf(params[2]);
        String realPrice = getTickerFromWebServer(fviFid);
        for (int deep = 4; deep > 0; deep--) {
            String room = "/trade-" + fviFid + "-" + deep;
            log.trace("push real data to room {}.", room);
            server.getRoomOperations(room).sendEvent("real", realPrice);
        }
    }

    private void pushEntrustData(String channel, String message) {
        // 推送挂单数据
        String[] params = channel.split(":");
        int fviFid = Integer.valueOf(params[0]);
        int type = Integer.valueOf(params[1]);
        int deep = Integer.valueOf(params[2]);
        String etype = type == 0 ? "entrust-buy" : "entrust-sell";

        // 限速，过滤重复内容，减少推送次数
        if (dataService.isNewMessage(channel, message)) {
            String data = dataService.getNewMessage(channel);
            if (data != null) {
                String room = "/trade-" + fviFid + "-" + deep;
                log.trace("push {} data to room {}.", etype, room);
                server.getRoomOperations(room).sendEvent(etype, dataService.getDepthEntrust(channel));
                sleep();
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushEntrustLog(String channel) {
        // 推送成交日志
        int fviFid = Integer.valueOf(channel.split(":")[2]);
        String entrustLog = dataService.getEntrustLog(fviFid);
        for (int deep = 4; deep > 0; deep--) {
            String room = "/trade-" + fviFid + "-" + deep;
            log.trace("push entrust-log data to room {}.", room);
            server.getRoomOperations(room).sendEvent("entrust-log", entrustLog);
        }
    }

    @Override
    public void close() {
        log.debug("close push server");
        try {
            if (messageCenterStarted) {
                messageCenter.close();
            }
        } catch (Exception e) {
        }
        try {
            if (serverStarted) {
                server.stop();
            }
        } catch (Exception e) {
        }
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
