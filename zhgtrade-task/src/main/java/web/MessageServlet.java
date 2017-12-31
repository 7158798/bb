package web;

import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-07-06 19:48
 */
public class MessageServlet extends HttpServlet {

    @Resource
    private MessageQueueService messageQueueService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String phone = req.getParameter("phone");
        String content = req.getParameter("content");

        boolean success = true;
        if (phone == null || content == null) {
            success = false;
        }

        if (success) {
            Map<String, Object> message = new HashMap<>();
            message.put("fphone", phone);
            message.put("fcontent", content);
            message.put("fcreateTime", System.currentTimeMillis());
            try {
                messageQueueService.publish(QueueConstants.MESSAGE_COMMON_QUEUE, message);
            } catch (Exception e) {
                success = false;
            }
        }

        resp.getWriter().write("{\"ret\": " + success + "}");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

}
