package com.chary.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Disabled
    @Test
    void testSendmail(){
        redisTemplate.opsForValue().set("email","nagulakarthik20@gmail.com");
        Object salary = redisTemplate.opsForValue().get("salary");
        //System.out.println("Value in Redis: " + email);

    }
}
