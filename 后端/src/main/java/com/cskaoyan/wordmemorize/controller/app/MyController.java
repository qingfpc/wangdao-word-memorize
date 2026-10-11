package com.cskaoyan.wordmemorize.controller.app;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MyController {

    private final ChatClient chatClient;

    // Spring自动注入ChatClient.Builder对象
    public MyController(ChatClient.Builder chatClientBuilder) {
        // 利用Builder对象的build()方法创建出ChatClient对象
        this.chatClient = chatClientBuilder.build();
    }
}
