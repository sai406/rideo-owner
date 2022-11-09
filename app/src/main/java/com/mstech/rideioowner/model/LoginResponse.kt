package com.mstech.rideioowner.model

data class LoginResponse(
    val Address: String,
    val Email: String,
    val FirstName: String,
    val LastName: String,
    val MerchnatId: Int,
    val Mobile: String,
    val OwnerId: String,
    val Pin: String,
    val Type: String
)