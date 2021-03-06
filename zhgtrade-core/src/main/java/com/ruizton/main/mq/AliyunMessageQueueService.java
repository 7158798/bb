package com.ruizton.main.mq;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.*;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-04-05 10:02
 */
public class AliyunMessageQueueService implements MessageQueueService {

    @Value("${ons.topic}")
    private String topic;
    @Value("${ons.accessKey}")
    private String accessKey;
    @Value("${ons.secretKey}")
    private String secretKey;
    @Value("${ons.producerId}")
    private String producerId;
    @Value("${ons.consumerId}")
    private String consumerId;

    private Producer producer;
    private Consumer consumer;

    @PostConstruct
    public void init() {
        System.out.println("初始化Ons");
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumeThreadNums, 1);  // 只有一个消费线程
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        properties.put(PropertyKeyConst.ProducerId, producerId);
        properties.put(PropertyKeyConst.ConsumerId, consumerId);
        consumer = ONSFactory.createConsumer(properties);
        producer = ONSFactory.createProducer(properties);
        producer.start();
        consumer.start();
        System.out.println("初始化Ons end");
    }

    @Override
    public void publish(String channel, Object message) {
        Message msg = new Message(
                topic,
                channel,
                JSON.toJSONString(message).getBytes());
        producer.send(msg);
    }

    @Override
    public <T> void subscribe(String channel, final MessageListener<T> listener, final Class<T> clazz) {
        consumer.subscribe(topic, channel, new com.aliyun.openservices.ons.api.MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
                String msgbody = new String(message.getBody());
                T msg = JSON.parseObject(msgbody, clazz);
                listener.onMessage(msg);
                return Action.CommitMessage;
            }
        });
    }

}
