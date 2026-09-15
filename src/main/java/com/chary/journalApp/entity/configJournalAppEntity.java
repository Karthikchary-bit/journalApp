package com.chary.journalApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config_journal_app")
//lombok maven dependency
@Data
@NoArgsConstructor
public class configJournalAppEntity {


    private String key;

    private String value;


}
