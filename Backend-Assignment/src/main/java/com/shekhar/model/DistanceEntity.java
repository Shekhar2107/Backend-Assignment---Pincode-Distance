package com.shekhar.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "distance_records")
public class DistanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originPincode;

    @Column(nullable = false)
    private String destinationPincode;

    @Column(nullable = false)
    private String travelMode;  

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal distance; 

    @Column(nullable = false)
    private String duration; 

    public DistanceEntity() {
    }

    public DistanceEntity(String originPincode, String destinationPincode, String travelMode, BigDecimal distance, String duration) {
        this.originPincode = originPincode;
        this.destinationPincode = destinationPincode;
        this.travelMode = travelMode;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getTravelMode() {  
        return travelMode;
    }

    public void setTravelMode(String travelMode) { 
        this.travelMode = travelMode;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
