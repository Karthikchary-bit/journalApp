package com.chary.journalApp.repository;

import com.chary.journalApp.entity.JournalEntry;
import com.chary.journalApp.entity.configJournalAppEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface configJournalApp extends MongoRepository<configJournalAppEntity, ObjectId> {
}
