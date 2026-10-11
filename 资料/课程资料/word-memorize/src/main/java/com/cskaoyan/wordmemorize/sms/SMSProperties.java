package com.cskaoyan.wordmemorize.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("sms")
@Data
public class SMSProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String sign;

    private String loginTemplateCode;

    private String remindTemplateCode;

    private String endPoint;

    // 准备好用来发送短信的client对象
    @Bean
    public Client getSmsClient() throws Exception {
        Config config = new Config();
        config.setAccessKeyId(accessKeyId);
        config.setAccessKeySecret(accessKeySecret);
        config.setEndpoint(endPoint);
        return new Client(config);
    }
}
