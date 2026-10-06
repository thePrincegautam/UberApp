package com.rideshare.matching_service.dto;

import lombok.Data;

/**
 * Response received from location service
 * When querying for  nearby drivers.
 */
@Data
public class NearByDriverResponse {
    private String driverId;
    private double latitude;
    private double longitude;
    private double distanceInKm;
}
