package com.zhgtrade.deal.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-05-10 22:16
 */
public class Config {

    private static Config INSTANCE = new Config();

    private Properties props = new Properties();

    public Config() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
            props.load(in);
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public static final Config getInstance() {
        return INSTANCE;
    }

}
