package com.mstech.rideioowner.utils

import com.mstech.rideiodriver.Model.PlaybackAlerts
import com.mstech.rideioowner.model.LoginResponse
import com.mstech.rideioowner.model.VehicleListResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("services/trackservices.asmx/OwnerLogin")
    fun loginMethod(@Query("mobile") mobile: String, @Query("pin") pin: String, @Query("mid") mid: String): Call<List<LoginResponse>>

    @GET("api/track/GetLiveTrack")
    fun allLiveVehicles(@Query("ownerid") ownerid: String, @Query("imei") imei: String) : Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("api/track/GetTripHistory")
    fun getTripHistory(@Body postdata : RequestBody):Call<PlaybackAlerts>
    @GET("api/track/GetOwnerVehicles")
    fun getOwnerVehiclelist(@Query("OwnerId") ownerId: String?): Call<List<VehicleListResponse?>>?


}