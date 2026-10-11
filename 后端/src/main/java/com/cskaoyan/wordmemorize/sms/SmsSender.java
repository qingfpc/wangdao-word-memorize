package com.cskaoyan.wordmemorize.sms;

import com.alibaba.fastjson2.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.cskaoyan.wordmemorize.common.constant.ResultCodeEnum;
import com.cskaoyan.wordmemorize.common.exception.BusinessException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class SmsSender {

    @Resource
    SMSProperties SMSProperties;

    @Resource
    Client smsClient;

    /*
         userName: 用户名(现在用不到了)
         vocCount: 剩余的待学习单词数量
         phoneNumber: 手机号
     */
    public void sendRemindMessage(String userName, Integer vocCount, String phoneNumber) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phoneNumber);
        sendSmsRequest.setSignName(SMSProperties.getSign());
        sendSmsRequest.setTemplateCode(SMSProperties.getRemindTemplateCode());

        HashMap<String, Object> map = new HashMap<>();
        if (vocCount < 999) {
            // 因为阿里云发送的验证码必须是4-6位的数值，所以这里将字符串统一转化为4位
            String vocCountStr = "%04d".formatted(vocCount);
            map.put("code", vocCountStr); // 我们把用户当天还未学习的单词量当成验证码发送
        } else {
            map.put("code", vocCount);
        }

        String jsonString = JSON.toJSONString(map);
        sendSmsRequest.setTemplateParam(jsonString);

        try {
            smsClient.sendSms(sendSmsRequest);
        } catch (Exception e) {
            throw new BusinessException(ResultCodeEnum.SEND_REMIND_MESSAGE_FAIL);
        }
    }
}
