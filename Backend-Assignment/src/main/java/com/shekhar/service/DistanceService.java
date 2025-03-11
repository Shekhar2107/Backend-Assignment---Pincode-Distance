package com.shekhar.service;

import com.shekhar.config.GoogleMapsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DistanceService {

    @Autowired
    private GoogleMapsConfig googleMapsConfig;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getDistance(String origin, String destination) {
        String apiKey = googleMapsConfig.getApiKey();
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origin + 
                     "&destinations=" + destination + "&key=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }
}
