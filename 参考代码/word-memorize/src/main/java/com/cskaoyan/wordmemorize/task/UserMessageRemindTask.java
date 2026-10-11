package com.cskaoyan.wordmemorize.task;

import com.cskaoyan.wordmemorize.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class UserMessageRemindTask {

    @Resource
    UserService userService;

    //@Scheduled(cron = "* 1 * * * ?")
    //@Scheduled(cron = "* 10 * * * ?")
    public void initialize() {
        log.info("start to message remind....");
        userService.messageRemind();
        log.info("end message remind....");
    }
}
