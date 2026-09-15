package com.chary.journalApp.service;

import com.chary.journalApp.entity.JournalEntry;
import com.chary.journalApp.enums.Sentiment;
import org.springframework.stereotype.Service;

@Service
public class SentimentAnalysisService {

    public void analyzeAndSetSentiment(JournalEntry entry) {
        Sentiment sentiment = getSentiment(entry.getContent());

        if (sentiment != null) {
            entry.setSentiment(sentiment);
            entry.setSentimentAnalyzed(true);
        } else {
            entry.setSentiment(null);
            entry.setSentimentAnalyzed(false);
        }
    }

    public Sentiment getSentiment(String content) {
        if (content == null || content.isEmpty()) return null;
        String text = content.toLowerCase();

        if (text.contains("happy") || text.contains("great") || text.contains("joy")) return Sentiment.HAPPY;
        if (text.contains("sad") || text.contains("bad") || text.contains("cry")) return Sentiment.SAD;
        if (text.contains("angry") || text.contains("mad") || text.contains("hate")) return Sentiment.ANGRY;
        if (text.contains("anxious") || text.contains("worry") || text.contains("nervous")) return Sentiment.ANXIOUS;

        return null;
    }
}
