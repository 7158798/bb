package com.ruizton.main.oss;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-07 19:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-oss.xml"})
public class SimpleAliyunServiceTest {

    @Autowired
    AliyunService aliyunService;

    @Test
    public void testUpdateFile() throws Exception {
        InputStream in = new FileInputStream("e:/test.png");
        aliyunService.updateFile(in, "test.png", "image/png");
        // http://zhgtrade.oss-cn-qingdao.aliyuncs.com/test.png
    }

    @Test
    public void testDeleteFile() throws Exception {
        aliyunService.deleteFile("test.png");
    }
}