package com.cskaoyan.wordmemorize.config;

import com.cskaoyan.wordmemorize.sms.SmsDelayMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.DelayQueue;

@Configuration
public class SmsQueueConfig {

    @Bean(name = "sms-delay-queue")
    public DelayQueue<SmsDelayMessage> smsDelayQueue() {
        return new DelayQueue<>();
    }
}
