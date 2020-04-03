package com.mstech.rideioowner.model

data class GeofenceListResponse(
    val centerLatitude: String,
    val centerLongitude: String,
    val createdDate: String,
    val driverId: Int,
    val enterExitAlert: Any,
    val geoFenceId: Int,
    val geoFenceName: String,
    val imei: String,
    val ownerId: Int,
    val radius: String
)