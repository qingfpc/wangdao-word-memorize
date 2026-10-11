package com.cskaoyan.wordmemorize;

//import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.cskaoyan.wordmemorize.dao.mapper")
@EnableAspectJAutoProxy
@EnableScheduling
//@EnableConfigurationProperties
public class WordMemorizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordMemorizeApplication.class, args);
    }

}
