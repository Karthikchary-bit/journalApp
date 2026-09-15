package com.chary.journalApp.scheduler;

import com.chary.journalApp.cache.Appcache;
import com.chary.journalApp.entity.JournalEntry;
import com.chary.journalApp.entity.User;
import com.chary.journalApp.enums.Sentiment;
import com.chary.journalApp.repository.UserRepositoryImpl;
import com.chary.journalApp.service.EmailService;
import com.chary.journalApp.service.SentimentAnalysisService;
import com.chary.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Map;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserScheduler {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService service;

    @Autowired
    private UserRepositoryImpl userRepository;

//    @Autowired
//    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private Appcache appcache;

    //@Scheduled(cron ="0 0 9 ? * SUN *")
    //@Scheduled(cron ="0 *  * ? *  *")

    public void fetchUsersAndSendSaMail() {
        List<User> users = userRepository.getUserForSA();
        System.out.println("DEBUG: Users found for SA: " + (users != null ? users.size() : "null"));

        if (users != null) {
            for (User user : users) {
                List<JournalEntry> journalEntries = user.getJournalEntries();
                System.out.println("DEBUG: Checking user " + user.getEmail() + " with " + (journalEntries != null ? journalEntries.size() : 0) + " total entries." +user.getUserName());

                // 1. Filter entries for the last 7 days
                List<Sentiment> sentiments = journalEntries.stream()
                        .filter(x -> x.getDate() != null && x.getDate().isAfter(LocalDateTime.now().minusDays(7)))
                        .map(JournalEntry::getSentiment)
                        .filter(sentiment -> sentiment != null)
                        .collect(Collectors.toList());

                System.out.println("DEBUG: Sentiments found in last 7 days: " + sentiments.size());

                if (!sentiments.isEmpty()) {
                    // 2. Calculate the most frequent sentiment
                    Map<Sentiment, Integer> sentimentCount = new HashMap<>();
                    for (Sentiment sentiment : sentiments) {
                        sentimentCount.put(sentiment, sentimentCount.getOrDefault(sentiment, 0) + 1);
                    }

                    Sentiment mostFrequentSentiment = null;
                    int maxCount = 0;
                    for (Map.Entry<Sentiment, Integer> entry : sentimentCount.entrySet()) {
                        if (entry.getValue() > maxCount) {
                            maxCount = entry.getValue();
                            mostFrequentSentiment = entry.getKey();
                        }
                    }

                    // 3. Send the email if a sentiment was determined
                    if (mostFrequentSentiment != null) {
                        System.out.println("DEBUG: Sending email to " + user.getEmail() + " with sentiment: " + mostFrequentSentiment);
                        emailService.sendEmail(user.getEmail(), "Sentiment of the last 7 days", mostFrequentSentiment.toString());
                    }
                } else {
                    System.out.println("DEBUG: No recent sentiment data for " + user.getEmail());
                }
            }
        }
    }
    @Scheduled(cron="0 0/10 * 1/1 * ? *")
    public void clearAppCache(){
        appcache.init();
    }
}