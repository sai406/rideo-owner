package com.mstech.rideioowner.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




data class VehicleListResponse(
    val address: String,
    val deviceId: String,
    val deviceName: String,
    val imei: String,
    val installationDate: String,
    val installationId: String,
    val installedDateFormat: String,
    val installerId: String,
    val latitude: Double,
    val longitude: Double,
    val ownerId: String,
    val ownerName: String,
    val simNumber: String,
    val vehicleNumber: String
)