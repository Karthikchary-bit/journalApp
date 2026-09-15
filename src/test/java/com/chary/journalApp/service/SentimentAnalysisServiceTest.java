package com.chary.journalApp.service;

import com.chary.journalApp.entity.JournalEntry;
import com.chary.journalApp.enums.Sentiment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SentimentAnalysisServiceTest {

    private final SentimentAnalysisService sentimentAnalysisService = new SentimentAnalysisService();

    @Test
    void analyzeAndSetSentimentShouldMarkEntryAsAnalyzed() {
        JournalEntry entry = new JournalEntry();

        sentimentAnalysisService.analyzeAndSetSentiment(entry);

        assertEquals(Sentiment.NEUTRAL, entry.getSentiment());
        assertTrue(entry.isSentimentAnalyzed());
    }
}
