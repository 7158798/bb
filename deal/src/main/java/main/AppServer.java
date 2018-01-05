package main;

import com.alibaba.druid.support.http.WebStatFilter;
import com.zhgtrade.deal.core.MessageCenter;
import com.zhgtrade.deal.http.RpcServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-06-11 22:03
 */
public class AppServer {

    private static final Logger log = LoggerFactory.getLogger(AppServer.class);

    public static void main(String[] args) throws Exception {
        System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                "                   _ooOoo_\n" +
                "                  o8888888o\n" +
                "                  88\" . \"88\n" +
                "                  (| -_- |)\n" +
                "                  O\\  =  /O\n" +
                "               ____/`---'\\____\n" +
                "             .'  \\\\|     |//  `.\n" +
                "            /  \\\\|||  :  |||//  \\\n" +
                "           /  _||||| -:- |||||-  \\\n" +
                "           |   | \\\\\\  -  /// |   |\n" +
                "           | \\_|  ''\\---/''  |   |\n" +
                "           \\  .-\\__  `-`  ___/-. /\n" +
                "         ___`. .'  /--.--\\  `. . __\n" +
                "      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".\n" +
                "     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |\n" +
                "     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /\n" +
                "======`-.____`-.___\\_____/___.-`____.-'======\n" +
                "                   `=---='\n" +
                "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                "");

        // 内嵌jetty, 提供web管理功能
        long startTime = System.currentTimeMillis();

        Application application = Application.getInstance();

//        Object server = application.getBean("org.eclipse.jetty.server.Server");

//        Object servletContext = application.getBean("org.eclipse.jetty.servlet.ServletContextHandler");
//        Object handlers = application.getBean("org.eclipse.jetty.server.handler.HandlerCollection");

//        servletContext.getClass().getMethod("setClassLoader", ClassLoader.class).invoke(servletContext, Thread.currentThread().getContextClassLoader());
//        servletContext.getClass().getMethod("setContextPath", String.class).invoke(servletContext, "/");
//        servletContext.getClass().getMethod("addServlet", String.class, String.class).invoke(servletContext, "com.alibaba.druid.support.http.StatViewServlet", "/druid/*");
//        servletContext.getClass().getMethod("addServlet", String.class, String.class).invoke(servletContext, "com.zhgtrade.deal.stat.IndexServlet", "/");

//        server.getClass().getMethod("setHandler", Class.forName("org.eclipse.jetty.server.Handler")).invoke(server, handlers);

//        server.getClass().getMethod("start").invoke(server);

        log.info("app server started {} ms", (System.currentTimeMillis() - startTime));

        System.out.println("deal started");

        MessageCenter messageCenter = application.getBean(MessageCenter.class);
        messageCenter.init();

//        server.getClass().getMethod("join").invoke(server);


        ServletContextHandler servletContext = new ServletContextHandler();
        servletContext.setContextPath("/");

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(1001);
        server.setConnectors(new Connector[] { connector });
        HandlerCollection handlerCollection = new HandlerCollection();

        ServletContextHandler springMvcHandler = new ServletContextHandler();
        springMvcHandler.setContextPath("/");
        springMvcHandler.addServlet(new ServletHolder("rpc-servlet", RpcServlet.class), "/*");
        // WEB监控
        FilterHolder webStatFilter = new FilterHolder(new WebStatFilter());
        webStatFilter.setInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        springMvcHandler.addFilter(webStatFilter, "/*", null);

        handlerCollection.setHandlers(new Handler[]{springMvcHandler});

        server.setHandler(handlerCollection);

        server.start();
        server.join();

    }

}
