package com.ruizton.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/3/21 0021.
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml", "classpath:applicationContext-mongo.xml", "classpath:applicationContext-rabbitmq.xml", "classpath:applicationContext-redis.xml"})
public class BaseTestCase {
}
