package com.shekhar.controller;

import com.shekhar.service.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/distance")
public class DistanceController {

    @Autowired
    private DistanceService distanceService;

    @GetMapping
    public String getDistance(
            @RequestParam String origin, 
            @RequestParam String destination) {
        return distanceService.getDistance(origin, destination);
    }
}
