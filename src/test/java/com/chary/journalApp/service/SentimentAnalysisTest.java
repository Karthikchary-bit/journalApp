package com.chary.journalApp.service;

import com.chary.journalApp.entity.JournalEntry;
import com.chary.journalApp.enums.Sentiment;
import com.chary.journalApp.scheduler.UserScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SentimentAnalysisTest {
    @Autowired
    private UserScheduler userScheduler;




    @Test
    void testSentimentMapping() {
        userScheduler.fetchUsersAndSendSaMail();
    }
}
