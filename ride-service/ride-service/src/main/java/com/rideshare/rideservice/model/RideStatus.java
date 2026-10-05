package com.rideshare.rideservice.model;

/**
 * flow
 * requested-> matching-> accepted-> driver_arriving
 * -> ride started -> completed
 * -> canncelled(can happen at multiple stages)
 */
public enum RideStatus {
    REQESTED,
    MATCHING,
    ACCEPTED,
    DRIVER_ARRIVING,
    RIDE_STARTED,
    COMPLETED,
    CANCLLED
}
