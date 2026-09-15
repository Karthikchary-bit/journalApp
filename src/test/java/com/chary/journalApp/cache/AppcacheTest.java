package com.chary.journalApp.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppcacheTest {

    @Test
    void initShouldPopulateWeatherApiTemplate() {
        Appcache appcache = new Appcache();
        appcache.init();

        String weatherApi = appcache.APP_CACHE.get(Appcache.keys.WEATHER_API.toString());
        assertTrue(weatherApi.contains("<city>"));
        assertTrue(weatherApi.contains("<apiKey>"));
    }
}
