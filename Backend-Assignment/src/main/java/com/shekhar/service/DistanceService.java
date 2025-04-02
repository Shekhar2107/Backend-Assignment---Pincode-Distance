package com.shekhar.service;

import com.google.gson.*;
import com.shekhar.model.DistanceEntity;
import com.shekhar.repository.DistanceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableCaching
public class DistanceService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @Value("${google.maps.routes.api.url}")
    private String routesApiUrl;

    @Value("${google.maps.geocode.api.url}")
    private String geocodeApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final DistanceRepository distanceRepository;

    public DistanceService(DistanceRepository distanceRepository) {
        this.distanceRepository = distanceRepository;
    }

    @Cacheable(value = "distanceCache", key = "#originPincode + '-' + #destinationPincode")
    public String getDistance(String originPincode, String destinationPincode) {
        // 1️----> Check if data already exists in the database
        List<DistanceEntity> existingRecords = distanceRepository.findByOriginPincodeAndDestinationPincode(originPincode, destinationPincode);
        if (!existingRecords.isEmpty()) {
            System.out.println("Returning cached DB data for " + originPincode + " to " + destinationPincode);
            return convertEntitiesToJson(existingRecords);
        }

        // 2️------> Fetch new data from API if not found in DB
        double[] originCoords = getCoordinates(originPincode);
        double[] destinationCoords = getCoordinates(destinationPincode);

        if (originCoords == null || destinationCoords == null) {
            return "{ \"error\": \"Invalid pincode(s)\" }";
        }

        String jsonResponse = callRoutesApi(originCoords, destinationCoords);

        // 3️-----> Save new data to database
        List<DistanceEntity> newRecords = saveRoutesToDatabase(jsonResponse, originPincode, destinationPincode);

        return convertEntitiesToJson(newRecords);
    }

    private double[] getCoordinates(String pincode) {
        String url = String.format("%s?address=%s&key=%s", geocodeApiUrl, pincode, apiKey);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();

            JsonArray results = jsonResponse.getAsJsonArray("results");
            if (results == null || results.size() == 0) {
                System.err.println("No results found for pincode: " + pincode);
                return null;
            }

            JsonObject location = results.get(0).getAsJsonObject()
                    .getAsJsonObject("geometry")
                    .getAsJsonObject("location");

            return new double[]{location.get("lat").getAsDouble(), location.get("lng").getAsDouble()};
        } catch (Exception e) {
            System.err.println("Error extracting coordinates: " + e.getMessage());
            return null;
        }
    }

    private String callRoutesApi(double[] origin, double[] destination) {
        String[] travelModes = {"DRIVE", "WALK", "BICYCLE", "TRANSIT"};
        JsonArray allRoutes = new JsonArray();

        for (String mode : travelModes) {
            try {
                JsonObject requestBodyJson = new JsonObject();

                JsonObject originLatLng = new JsonObject();
                originLatLng.addProperty("latitude", origin[0]);
                originLatLng.addProperty("longitude", origin[1]);

                JsonObject originLocation = new JsonObject();
                originLocation.add("latLng", originLatLng);

                JsonObject originFinal = new JsonObject();
                originFinal.add("location", originLocation);

                JsonObject destLatLng = new JsonObject();
                destLatLng.addProperty("latitude", destination[0]);
                destLatLng.addProperty("longitude", destination[1]);

                JsonObject destLocation = new JsonObject();
                destLocation.add("latLng", destLatLng);

                JsonObject destinationFinal = new JsonObject();
                destinationFinal.add("location", destLocation);

                requestBodyJson.add("origin", originFinal);
                requestBodyJson.add("destination", destinationFinal);
                requestBodyJson.addProperty("travelMode", mode);
                requestBodyJson.addProperty("computeAlternativeRoutes", true);

                HttpHeaders headers = new HttpHeaders();
                headers.set("X-Goog-Api-Key", apiKey);
                headers.set("X-Goog-FieldMask", "routes.distanceMeters,routes.duration");
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> entity = new HttpEntity<>(requestBodyJson.toString(), headers);
                ResponseEntity<String> response = restTemplate.exchange(routesApiUrl, HttpMethod.POST, entity, String.class);

                JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();

                if (!jsonResponse.has("routes") || jsonResponse.get("routes").isJsonNull()) {
                    System.out.println("No routes found for mode: " + mode);
                    continue;
                }

                JsonArray routes = jsonResponse.getAsJsonArray("routes");

                for (JsonElement routeElement : routes) {
                    JsonObject route = routeElement.getAsJsonObject();

                    int distanceMeters = route.get("distanceMeters").getAsInt();
                    String durationStr = route.get("duration").getAsString();
                    long durationSeconds = Long.parseLong(durationStr.replace("s", ""));

                    String formattedDuration = String.format("%d hours %d minutes",
                            durationSeconds / 3600,
                            (durationSeconds % 3600) / 60);

                    JsonObject routeData = new JsonObject();
                    routeData.addProperty("travelMode", mode);
                    routeData.addProperty("distance", String.format("%.2f km", distanceMeters / 1000.0));
                    routeData.addProperty("duration", formattedDuration);

                    allRoutes.add(routeData);
                }
            } catch (Exception e) {
                System.err.println("Error calling Routes API for " + mode + ": " + e.getMessage());
            }
        }

        JsonObject finalResponse = new JsonObject();
        finalResponse.add("routes", allRoutes);
        return finalResponse.toString();
    }

    private List<DistanceEntity> saveRoutesToDatabase(String jsonResponse, String originPincode, String destinationPincode) {
        JsonObject responseJson = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonArray routesArray = responseJson.getAsJsonArray("routes");

        List<DistanceEntity> entities = new ArrayList<>();
        for (JsonElement element : routesArray) {
            JsonObject route = element.getAsJsonObject();

            DistanceEntity entity = new DistanceEntity();
            entity.setOriginPincode(originPincode);
            entity.setDestinationPincode(destinationPincode);
            entity.setTravelMode(route.get("travelMode").getAsString());
            entity.setDistance(BigDecimal.valueOf(Double.parseDouble(route.get("distance").getAsString().replace(" km", ""))));
            entity.setDuration(route.get("duration").getAsString());

            entities.add(entity);
        }

        return distanceRepository.saveAll(entities);
    }

    private String convertEntitiesToJson(List<DistanceEntity> entities) {
        JsonArray jsonArray = new JsonArray();
        for (DistanceEntity entity : entities) {
            JsonObject obj = new JsonObject();
            obj.addProperty("travelMode", entity.getTravelMode());
            obj.addProperty("distance", entity.getDistance().toString() + " km");
            obj.addProperty("duration", entity.getDuration());
            jsonArray.add(obj);
        }

        JsonObject finalJson = new JsonObject();
        finalJson.add("routes", jsonArray);
        return finalJson.toString();
    }
}
