package web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-06-11 22:03
 */
public class AppServer implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(AppServer.class);

    private Object server;
    private Object servletContext;

    public void init() throws Exception {
        // 内嵌jetty, 提供web管理功能

        server = getBean("org.eclipse.jetty.server.Server");

        servletContext = getBean("org.eclipse.jetty.servlet.ServletContextHandler");
        Object handlers = getBean("org.eclipse.jetty.server.handler.HandlerCollection");

        servletContext.getClass().getMethod("setClassLoader", ClassLoader.class).invoke(servletContext, Thread.currentThread().getContextClassLoader());
        servletContext.getClass().getMethod("setContextPath", String.class).invoke(servletContext, "/");

        addServlet("com.alibaba.druid.support.http.StatViewServlet", "/druid/*");
        addServlet(getBean("web.MessageServlet"), "/api/sendMessage");
        addServlet(getBean("web.EmailServlet"), "/api/sendEmail");

        server.getClass().getMethod("setHandler", Class.forName("org.eclipse.jetty.server.Handler")).invoke(server, handlers);

    }

    public void addServlet(String servlet, String path) throws Exception {
        servletContext.getClass().getMethod("addServlet", String.class, String.class).invoke(servletContext, servlet, path);
    }

    public void addServlet(Object servlet, String path) throws Exception {
        Class<?> holderClass = Class.forName("org.eclipse.jetty.servlet.ServletHolder");
        Object holder = holderClass.newInstance();
        holderClass.getMethod("setServlet", Class.forName("javax.servlet.Servlet")).invoke(holder, servlet);

        servletContext.getClass().getMethod("addServlet", holderClass, String.class).invoke(servletContext, holder, path);
    }

    public void autoStart() throws Exception {
        init();
        start();
        new Thread(){
            @Override
            public void run() {
                try {
                    log.info("started jetty server.");
                    join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void start() throws Exception {
        long startTime = System.currentTimeMillis();
        server.getClass().getMethod("start").invoke(server);
        log.info("app server started {} ms", (System.currentTimeMillis() - startTime));
    }

    public void join() throws Exception {
        server.getClass().getMethod("join").invoke(server);
    }

    private Object getBean(String clazz) throws Exception {
        return context.getBean(Class.forName(clazz));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private ApplicationContext context;

}
