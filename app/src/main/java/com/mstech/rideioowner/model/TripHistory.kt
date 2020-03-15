package com.mstech.rideiodriver.Model

data class TripHistory(
    val address: String,
    val angle: String,
    val dataDay: String,
    val dataUnixTime: String,
    val deviceName: Any,
    val event: String,
    val gpsOdometer: String,
    val ignitionStatus: String,
    val imei: String,
    val latitude: Double,
    val latitudeDirection: String,
    val longitude: Double,
    val longitudeDirection: String,
    val packetTypeId: String,
    val satellites: String,
    val sno: String,
    val speedKmph: String,
    val vehicleEvent: String,
    val vehicleNumber: Any
)