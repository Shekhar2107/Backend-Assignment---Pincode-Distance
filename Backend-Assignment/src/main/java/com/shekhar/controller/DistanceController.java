package com.shekhar.controller;

import com.shekhar.service.DistanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling distance calculation requests.
 */
@RestController
@RequestMapping("/api/distance")
public class DistanceController {

    private final DistanceService distanceService;

    public DistanceController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    /**
     * Calculates and retrieves the distance between two pincodes using POST.
     *
     * @param request The request containing origin and destination pincodes.
     * @return JSON response containing distances and durations.
     */
    @PostMapping("/calculate")
    public ResponseEntity<String> calculateDistance(@RequestBody DistanceRequest request) {
        String result = distanceService.getDistance(request.getOriginPincode(), request.getDestinationPincode());
        return ResponseEntity.ok(result);
    }

    // Inner class for request body
    public static class DistanceRequest {
        private String originPincode;
        private String destinationPincode;

        public String getOriginPincode() {
            return originPincode;
        }

        public void setOriginPincode(String originPincode) {
            this.originPincode = originPincode;
        }

        public String getDestinationPincode() {
            return destinationPincode;
        }

        public void setDestinationPincode(String destinationPincode) {
            this.destinationPincode = destinationPincode;
        }
    }
}
