package com.lc.jobs.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class SmsJob implements Runnable {

    public static final String MESSAGE_NAME = "messageName";
    public static final String MESSAGE_PASSWORD = "messagePassword";
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final int SUCCESS = 2;      // 短信发送成功
    private final int FAIL = 3;         // 短信发送失败
    private final int LIMIT = 800;      // 每次最大发送条数

    @Resource
    private ExecutorService executorService;
    @Resource
    private SystemArgsService systemArgsService;
    @Resource
    private JdbcTemplate jdbc;

    private void updateStatus(Object id, int status) {
        try {
            jdbc.update("UPDATE fvalidatemessage SET fstatus = ?, fSendTime = ? WHERE fid = ?", status, new Timestamp(System.currentTimeMillis()), id);
        } catch (Exception e) {
            log.error("更新短信状态失败", e);
        }
    }

    private void batSend() throws Exception {
        List<Map<String, Object>> list = jdbc.queryForList("SELECT * FROM fvalidatemessage WHERE fstatus = 1 AND type = 0 limit ?", LIMIT);
        if (list != null && list.size() > 0) {
            Map<String, String> account = systemArgsService.getSystemArgs(MESSAGE_NAME, MESSAGE_PASSWORD);
            final CountDownLatch latch = new CountDownLatch(list.size());
            list.forEach(map -> {
                executorService.submit(() -> {
                    int status = sendMessage(account, map.get("fPhone"), map.get("fContent")) ? SUCCESS : FAIL;
                    updateStatus(map.get("fid"), status);
                    latch.countDown();
                });
            });
            latch.await();
        }
    }

    private boolean sendMessage(Map<String, String> account, Object phone, Object content) {
        try {
            if (phone == null || content == null) {
                return false;
            }
            log.debug("发送短信到{}", phone);
            return send(account.get(MESSAGE_NAME), account.get(MESSAGE_PASSWORD), phone.toString(), content.toString());
        } catch (Exception e) {
            log.error("发送短信失败", e);
        }
        return false;
    }

    private boolean send(String name, String password, String phone, String content){
        boolean flag = false ;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("Uid", name.trim());
            map.put("Key", password.trim());
            map.put("smsMob", phone.trim());
            map.put("smsText", content.trim());
            String result = HttpClientUtils.post("http://gbk.sms.webchinese.cn", map, "gbk");

            if (Integer.parseInt(result) > 0){
                flag = true ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @PostConstruct
    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        log.debug("批量发送短信");
        try {
            batSend();
        } catch (Exception e) {
            log.error("批量发送短信 {}", e.toString());
        }
        log.debug("批量发送短信耗时: {}", (System.currentTimeMillis() - beginTime));
    }


}
