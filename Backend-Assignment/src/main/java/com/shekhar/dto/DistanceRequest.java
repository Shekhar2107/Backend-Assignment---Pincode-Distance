package com.shekhar.dto;

public class DistanceRequest {
    private String originPincode;
    private String destinationPincode;

    // Default constructor (needed for JSON deserialization)
    public DistanceRequest() {}

    public String getOriginPincode() {
        return originPincode;
    }

    public String getDestinationPincode() {
        return destinationPincode;
    }

    // Setter methods for Spring to map JSON properly
    public void setOriginPincode(String originPincode) {
        this.originPincode = originPincode;
    }

    public void setDestinationPincode(String destinationPincode) {
        this.destinationPincode = destinationPincode;
    }
}
