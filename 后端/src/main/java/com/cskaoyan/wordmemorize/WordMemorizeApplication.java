package com.cskaoyan.wordmemorize;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.cskaoyan.wordmemorize.dao.mapper")
@EnableAspectJAutoProxy
public class WordMemorizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordMemorizeApplication.class, args);
    }

}
