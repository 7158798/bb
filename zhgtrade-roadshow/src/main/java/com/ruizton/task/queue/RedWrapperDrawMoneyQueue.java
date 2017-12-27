package com.ruizton.task.queue;

import com.ruizton.main.dto.SubRedWrapperDTO;
import com.ruizton.main.model.RedWrapper;
import com.ruizton.main.model.SubRedWrapper;
import com.ruizton.main.mq.MessageListener;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.roadshow.RedWrapperService;
import com.ruizton.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 红包异步入账
 *
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/26
 */
public class RedWrapperDrawMoneyQueue implements MessageListener<SubRedWrapperDTO> {
    private Logger logger = LoggerFactory.getLogger(RedWrapperDrawMoneyQueue.class);

    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private RedWrapperService redWrapperService;

    @PostConstruct
    public void init() {
        messageQueueService.subscribe(QueueConstants.RED_WRAPPER_DRAW_MONEY_QUEUE, this, SubRedWrapperDTO.class);
    }

    @Override
    public void onMessage(SubRedWrapperDTO dto) {
        try{
            logger.debug("draw money of red wrapper to account, red id:" + dto.getRedWrapperId());
            RedWrapper redWrapper = redWrapperService.findById(dto.getRedWrapperId());
            SubRedWrapper subRedWrapper = new SubRedWrapper(redWrapper.getId(), dto.getUserId(), dto.getAmount(), dto.getCatchTime(), Utils.getTimestamp());
            redWrapperService.addRedWrapperMoneyToWallet(subRedWrapper);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("got a error when draw money of red wrapper to account, red id:" + dto.getRedWrapperId() + " userId:" + dto.getUserId(), e);
        }
    }
}
