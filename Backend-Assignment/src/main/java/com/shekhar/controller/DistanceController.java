package com.shekhar.controller;

import com.shekhar.service.DistanceService;
import com.shekhar.dto.DistanceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")  // Base URL
@RequiredArgsConstructor
public class DistanceController {

    private final DistanceService distanceService;

    @PostMapping("/calculate-distance")
    public ResponseEntity<Map<String, Object>> calculateDistance(@RequestBody DistanceRequest request) {
        System.out.println("📩 Received Distance Request: " + request);

        String responseJson = distanceService.getDistance(request.getOriginPincode(), request.getDestinationPincode());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", responseJson);

        return ResponseEntity.ok(response);
    }
}
