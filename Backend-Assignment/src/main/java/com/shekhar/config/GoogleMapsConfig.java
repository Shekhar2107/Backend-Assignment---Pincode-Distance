package com.shekhar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GoogleMapsConfig {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @Value("${google.maps.routes.api.url}")
    private String routesApiUrl;

    @Value("${google.maps.geocoding.api.url}")
    private String geocodingApiUrl;

    public String getApiKey() {
        return apiKey;
    }

    public String getRoutesApiUrl() {
        return routesApiUrl;
    }

    public String getGeocodingApiUrl() {
        return geocodingApiUrl;
    }
}
