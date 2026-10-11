package com.cskaoyan.wordmemorize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Configuration
public class SpelConfig {


    @Bean
    public SpelExpressionParser parser() {
        return new SpelExpressionParser();
    }
}
