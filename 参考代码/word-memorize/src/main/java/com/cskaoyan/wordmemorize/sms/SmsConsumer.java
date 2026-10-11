package com.cskaoyan.wordmemorize.sms;

import com.cskaoyan.wordmemorize.dao.entity.UserRemindMessageDO;
import com.cskaoyan.wordmemorize.dao.mapper.UserRemindMessageMapper;
import com.cskaoyan.wordmemorize.sms.SmsDelayMessage;
import com.cskaoyan.wordmemorize.sms.SmsSender;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.DelayQueue;

@Component
@Slf4j
public class SmsConsumer {


    @Resource
    DelayQueue<SmsDelayMessage> delayQueue;


    @Resource
    TaskExecutor taskExecutor;

    @Resource
    SmsSender smsSender;

    @Resource
    UserRemindMessageMapper userRemindMessageMapper;


    //@PostConstruct
    public void init() {
        log.info("start to consume message...");
        taskExecutor.execute(() -> {

            while (true) {
                try {
                    // 如果没有消息会在这里阻塞
                    SmsDelayMessage smsMessage = delayQueue.take();
                    log.info("获取到消息:{}", smsMessage);

                    // 发送消息
                    smsSender.sendRemindMessage(smsMessage.getUserName(), smsMessage.getVocCount(), smsMessage.getPhoneNumber());

                    // 添加插入记录
                    UserRemindMessageDO entity = new UserRemindMessageDO();
                    entity.setUserId(smsMessage.getUserId());
                    entity.setSendDate(LocalDate.now());
                    entity.setPhoneNumber(smsMessage.getPhoneNumber());
                    userRemindMessageMapper.insert(entity);
                } catch (Exception e) {
                    log.error("消息发送失败, 错误{}", e.getMessage());
                }
            }

        });
    }

}
