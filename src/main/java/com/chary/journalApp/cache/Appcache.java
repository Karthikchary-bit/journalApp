package com.chary.journalApp.cache;

import com.chary.journalApp.entity.configJournalAppEntity;
import com.chary.journalApp.repository.configJournalApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class Appcache {

    public enum keys{
        WEATHER_API;
    }
    @Autowired
    private configJournalApp configJournalApp;
    public Map<String,String> APP_CACHE;
    @PostConstruct
    public void init(){
        APP_CACHE = new HashMap<>();
        List<configJournalAppEntity> all= configJournalApp.findAll();
        for (configJournalAppEntity configJournalAppEntity : all) {
            APP_CACHE.put(configJournalAppEntity.getKey(),configJournalAppEntity.getValue());

        }

    }
}
