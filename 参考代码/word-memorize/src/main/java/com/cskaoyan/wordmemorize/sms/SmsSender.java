package com.cskaoyan.wordmemorize.sms;

import com.alibaba.fastjson2.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
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

    public void sendRemindMessage(String userName, Integer vocCount, String phoneNumber) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phoneNumber);
        sendSmsRequest.setSignName(SMSProperties.getSign());
        sendSmsRequest.setTemplateCode(SMSProperties.getRemindTemplateCode());

        HashMap<String, Object> map = new HashMap<>();
        //map.put("name", userName);
        //map.put("count", vocCount);
        // 0 ——> 0000
        String vocCountStr = String.format("%04d", vocCount);
        map.put("code", vocCountStr);
        String jsonString = JSON.toJSONString(map);
        sendSmsRequest.setTemplateParam(jsonString);


        try {
            SendSmsResponse sendSmsResponse = smsClient.sendSms(sendSmsRequest);
            System.out.println(sendSmsResponse);
        } catch (Exception e) {
            throw new BusinessException(ResultCodeEnum.SEND_REMIND_MESSAGE_FAIL);
        }
    }
}
