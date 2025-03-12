package com.shekhar.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@Service
public class DistanceService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @Value("${google.maps.routes.api.url}")
    private String routesApiUrl;

    @Value("${google.maps.geocode.api.url}")
    private String geocodeApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getDistance(String originPincode, String destinationPincode) {
        double[] originCoords = getCoordinates(originPincode);
        double[] destinationCoords = getCoordinates(destinationPincode);

        if (originCoords == null || destinationCoords == null) {
            return "{ \"error\": \"Invalid pincode(s)\" }";
        }

        return callRoutesApi(originCoords, destinationCoords);
    }

    private double[] getCoordinates(String pincode) {
        String url = String.format("%s?address=%s&key=%s", geocodeApiUrl, pincode, apiKey);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            System.out.println("Geocode API Response: " + response.getBody());
            JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();

            if (!jsonResponse.has("results") || jsonResponse.getAsJsonArray("results").size() == 0) {
                System.out.println("Error: No results found for pincode " + pincode);
                return null;
            }

            JsonObject location = jsonResponse.getAsJsonArray("results")
                    .get(0).getAsJsonObject().getAsJsonObject("geometry").getAsJsonObject("location");

            return new double[]{location.get("lat").getAsDouble(), location.get("lng").getAsDouble()};
        } catch (Exception e) {
            System.out.println("Error extracting coordinates: " + e.getMessage());
            return null;
        }
    }

    private String callRoutesApi(double[] origin, double[] destination) {
        JsonObject requestBodyJson = new JsonObject();
        
        // Origin
        requestBodyJson.add("origin", createLocationObject(origin));
        requestBodyJson.add("destination", createLocationObject(destination));
        requestBodyJson.addProperty("travelMode", "DRIVE");
    
        // Debugging
        System.out.println("🚀 Routes API Request: " + requestBodyJson.toString());
    
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Goog-Api-Key", apiKey);
        headers.set("X-Goog-FieldMask", "routes.distanceMeters,routes.duration");
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        try {
            HttpEntity<String> entity = new HttpEntity<>(requestBodyJson.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(routesApiUrl, HttpMethod.POST, entity, String.class);
    
            System.out.println("✅ Routes API Response: " + response.getBody());
    
            JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
            JsonObject route = jsonResponse.getAsJsonArray("routes").get(0).getAsJsonObject();
    
            int distanceMeters = route.get("distanceMeters").getAsInt();
            String durationStr = route.get("duration").getAsString(); // Example: "83040s"
    
            // ✅ Fix: Extract numeric duration and format correctly
            long durationSeconds = Long.parseLong(durationStr.replace("s", "")); // Remove 's' and parse
            String formattedDuration = String.format("%d hours %d minutes", durationSeconds / 3600, (durationSeconds % 3600) / 60);
    
            return String.format("{ \"distance\": \"%.2f km\", \"duration\": \"%s\" }", distanceMeters / 1000.0, formattedDuration);
        } catch (Exception e) {
            System.err.println("❌ Error parsing duration: " + e.getMessage());
            return "{ \"error\": \"Failed to fetch route data\" }";
        }
    }
    
    // ✅ Helper method to create location object
    private JsonObject createLocationObject(double[] coordinates) {
        JsonObject latLng = new JsonObject();
        latLng.addProperty("latitude", coordinates[0]);
        latLng.addProperty("longitude", coordinates[1]);
    
        JsonObject location = new JsonObject();
        location.add("latLng", latLng);
    
        JsonObject finalLocation = new JsonObject();
        finalLocation.add("location", location);
    
        return finalLocation;
    }
    
}