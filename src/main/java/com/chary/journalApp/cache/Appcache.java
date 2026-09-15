package com.chary.journalApp.cache;

import com.chary.journalApp.constants.PlaceHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class Appcache {

    public final Map<String, String> APP_CACHE = new HashMap<>();

    public enum keys {
        WEATHER_API
    }

    @PostConstruct
    public void init() {
        APP_CACHE.put(keys.WEATHER_API.toString(),
                "http://api.weatherstack.com/current?access_key=" + PlaceHolder.API_KEY + "&query=" + PlaceHolder.CITY);
    }
}
