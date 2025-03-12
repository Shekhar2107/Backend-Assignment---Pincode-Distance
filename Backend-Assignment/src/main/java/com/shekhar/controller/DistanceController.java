package com.shekhar.controller;

import com.shekhar.service.DistanceService;
import com.shekhar.dto.DistanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")  // Base URL
public class DistanceController {

    @Autowired
    private DistanceService distanceService;

    @PostMapping("/calculate-distance")
    public ResponseEntity<String> calculateDistance(@RequestBody DistanceRequest request) {
        String response = distanceService.getDistance(request.getOriginPincode(), request.getDestinationPincode());
        return ResponseEntity.ok(response);
    }
}
