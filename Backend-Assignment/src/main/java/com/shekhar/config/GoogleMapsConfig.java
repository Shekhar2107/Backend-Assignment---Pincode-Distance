package com.shekhar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component  // Makes it a Spring-managed bean
@Configuration
public class GoogleMapsConfig {

    private final String apiKey;
    private final String routesApiUrl;
    private final String geocodingApiUrl;

    public GoogleMapsConfig(
            @Value("${google.maps.api.key}") String apiKey,
            @Value("${google.maps.routes.api.url}") String routesApiUrl,
            @Value("${google.maps.geocoding.api.url}") String geocodingApiUrl) {
        this.apiKey = apiKey;
        this.routesApiUrl = routesApiUrl;
        this.geocodingApiUrl = geocodingApiUrl;
    }

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
