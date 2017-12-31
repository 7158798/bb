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
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-07-06 19:48
 */
public class EmailServlet extends HttpServlet {

    @Resource
    private MessageQueueService messageQueueService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String address = req.getParameter("address");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        boolean success = true;

        if (address == null || title == null || content == null) {
            success = false;
        }

        if (success) {
            Map<String, Object> message = new HashMap<>();
            message.put("email", address);
            message.put("ftitle", title);
            message.put("fcontent", content);
            message.put("fcreateTime", System.currentTimeMillis());
            try {
                messageQueueService.publish(QueueConstants.EMAIL_COMMON_QUEUE, message);
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
