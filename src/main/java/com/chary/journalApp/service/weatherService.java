package com.chary.journalApp.service;

import com.chary.journalApp.api.response.WeatherResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class weatherService {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String apikey=dotenv.get("API_KEY");
    private static final String Api="http://api.weatherstack.com/current?access_key=YOUR_ACCESS_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;
    public WeatherResponse getWeather(String city){

        String finalApi = Api.replace("CITY",city).replace("YOUR_ACCESS_KEY",apikey);
        System.out.println("API============"+finalApi);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();

        return body;
    }
}
