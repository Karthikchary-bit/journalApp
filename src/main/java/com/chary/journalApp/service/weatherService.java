package com.chary.journalApp.service;

import com.chary.journalApp.api.response.WeatherResponse;
import com.chary.journalApp.cache.Appcache;
import com.chary.journalApp.constants.PlaceHolder;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class weatherService {

    @Autowired
    private RidisService ridisService;
    //private static final Dotenv dotenv = Dotenv.load();
    //private static final String apikey=dotenv.get("API_KEY");
    @Value("${weather.api.key}")
    private String apikey;
//    private static final String Api="http://api.weatherstack.com/current?access_key=YOUR_ACCESS_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Appcache appcache;
    public WeatherResponse getWeather(String city){

        WeatherResponse weatherResponse = ridisService.get("weather_of_" + city, WeatherResponse.class);
        if(weatherResponse!=null){
            return weatherResponse;
        }else{
            String finalApi = appcache.APP_CACHE.get(Appcache.keys.WEATHER_API.toString()).replace(PlaceHolder.CITY,city).replace(PlaceHolder.API_KEY,apikey);

            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if(body!=null){
                ridisService.set("weather_of_" + city,body,300l);
            }
            return body;
        }


    }
}
