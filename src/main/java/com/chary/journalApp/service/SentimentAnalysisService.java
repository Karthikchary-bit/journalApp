package com.chary.journalApp.service;

import com.chary.journalApp.entity.JournalEntry;
import com.chary.journalApp.enums.Sentiment;
import org.springframework.stereotype.Service;

@Service
public class SentimentAnalysisService {

    public void analyzeAndSetSentiment(JournalEntry entry) {
        if (entry == null) {
            return;
        }
        entry.setSentiment(Sentiment.NEUTRAL);
        entry.setSentimentAnalyzed(true);
    }
}
