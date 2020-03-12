package com.mstech.rideioowner.utils

import com.mstech.rideioowner.model.LoginResponse
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

@GET("services/trackservices.asmx/OwnerLogin")

fun loginMethod(@Query("mobile") mobile:String,@Query("pin") pin:String,@Query("mid") mid:String):Call<List<LoginResponse>>

}