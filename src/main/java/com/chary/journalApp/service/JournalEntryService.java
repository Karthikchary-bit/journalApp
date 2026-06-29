package com.chary.journalApp.service;

import com.chary.journalApp.entity.JournalEntry;
import com.chary.journalApp.entity.User;
import com.chary.journalApp.repository.journalEntryRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Slf4j
@Component
public class JournalEntryService {


    @Autowired
    private journalEntryRepo journalEntryRepo;
    @Autowired
    private UserService userService;
    @Transactional// This method is now safe. If saving the journal succeeds,
    // but saving the user fails, the journal save is undone!
    public void saveEntry(JournalEntry journalEntry,String userName){
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException("An error has occurred while saving the entry.",e);
        }

    }
    public void saveEntry(JournalEntry journalEntry){

        journalEntry.setDate(LocalDateTime.now());
         journalEntryRepo.save(journalEntry);


    }
    public List<JournalEntry> getAll(){
        return journalEntryRepo.findAll();
    }
    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepo.findById(id);
    }
    @Transactional
    public void deleteById(ObjectId id,String userName){
        try {
            User user = userService.findByUserName(userName);
            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepo.deleteById(id);
            }
        } catch (Exception e) {
            log.error("Error",e);
            throw new RuntimeException("AN error occurred while deleting the entry",e);
        }

    }

}
