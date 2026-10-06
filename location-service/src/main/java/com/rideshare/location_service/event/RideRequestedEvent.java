package com.rideshare.location_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * event consumed from Kafka topic : ride:requested
 * published by ride service when a rider requests a ride
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class RideRequestedEvent {
    private String riderId;
    private String rideId;
    private double pickupLatitude;
    private double pickupLongitude;
    private String pickupAddress;
    private double dropLatitude;
    private double dropLongitude;
    private String dropAddress;
}
