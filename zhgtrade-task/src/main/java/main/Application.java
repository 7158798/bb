package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-05-31 14:53
 */
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("start task application");
        long beginTime = System.currentTimeMillis();
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
//            String[] beans = context.getBeanDefinitionNames();
//            System.out.println("contains beans:");
//            for (String bean : beans) {
//                System.out.println(bean);
//            }
            System.out.println("started!");
            logger.info("start task application successful " + (System.currentTimeMillis() - beginTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("start task application unsuccessful reason " + e.getLocalizedMessage());
            System.exit(-1);
        }

    }

}
