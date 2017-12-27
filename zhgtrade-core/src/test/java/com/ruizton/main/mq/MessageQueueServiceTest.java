package com.ruizton.main.mq;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ruizton.main.model.Fvalidateemail;
/**
 * 监听短信
 * @author yujie
 *
 */
public class MessageQueueServiceTest {

    public static void main(String[] args) {
    	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext-redis.xml");
    	MessageQueueService messageQueueService = context.getBean(MessageQueueService.class);
    	System.out.println("send messafge");
     
    	// ========== 监听 ==========
    	/*messageQueueService.subscribe(QueueConstants.MESSAGE_COMMON_QUEUE, new MessageListener<Fvalidatemessage>() {
        @Override
	        public void onMessage(Fvalidatemessage message) {
	        	System.out.println("receive");
	        	System.out.println(message.getFphone());
	        	System.out.println(message.getFcontent());
	        }
    	}, Fvalidatemessage.class);*/
    	messageQueueService.subscribe(QueueConstants.EMAIL_COMMON_QUEUE, new MessageListener<Fvalidateemail>() {
    	@Override
	    	public void onMessage(Fvalidateemail message) {	
	    		System.out.println("receive email validate");
	    		System.out.println(message.getFcontent());
	    	}	
        }, Fvalidateemail.class);
    }
}	