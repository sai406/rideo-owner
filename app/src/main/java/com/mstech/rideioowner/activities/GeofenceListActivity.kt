package com.mstech.rideioowner.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.rideioowner.R
import com.mstech.rideioowner.adapter.GeofenceListAdapter
import com.mstech.rideioowner.model.GeofenceListResponse
import com.mstech.rideioowner.model.SharedKey
import com.mstech.rideioowner.utils.MyUtils
import com.mstech.rideioowner.utils.RetrofitFactory
import kotlinx.android.synthetic.main.activity_geofence_list.*
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class GeofenceListActivity : BaseActivity() {
    var geofencelist: List<GeofenceListResponse> =
        ArrayList<GeofenceListResponse>()
    var imei =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofence_list)
        setScreenTitle("Geofences")
        getBackButton().setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        getHomeButton().setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        })
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = linearLayoutManager
        try {
            imei = intent.getStringExtra("imei")
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
        if(NetworkUtils.isConnected()){
            getGeofenceList()
        }else{
            ToastUtils.showShort("No Internet Connection")
        }
        floatingActionButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,GeofenceActivity::class.java).putExtra("imei",imei))
        })
    }

    private fun getGeofenceList() {
        MyUtils.showProgress(this, true)
        var obj = JSONObject()
        obj.put("OwnerId", SPStaticUtils.getString(SharedKey.OWNER_ID,""))
        obj.put("Imei", imei)
        var body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            ((obj)).toString()
        );
        RetrofitFactory.client
            .getgeofenceList(
                body
            )
            .enqueue(object : Callback<List<GeofenceListResponse>> {
                override fun onResponse(
                    call: Call<List<GeofenceListResponse>>,
                    response: Response<List<GeofenceListResponse>>
                ) {
                    MyUtils.showProgress(this@GeofenceListActivity, false)
                    if (response.isSuccessful
                    ) {
                        LogUtils.e(response.body())
                        try {
                            geofencelist = response.body() as List<GeofenceListResponse>
                            var adapter: GeofenceListAdapter? =
                                geofencelist?.let { GeofenceListAdapter(this@GeofenceListActivity,
                                    it as MutableList<GeofenceListResponse>
                                ) }
                            recyclerView?.adapter = adapter
                            adapter?.notifyDataSetChanged()
                            if (geofencelist!!.size > 0) {
                            } else {
                                ToastUtils.showShort("No Geofences Found")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                override fun onFailure(
                    call: Call<List<GeofenceListResponse>>,
                    t: Throwable
                ) {
                    ToastUtils.showShort(t.message)
                    MyUtils.showProgress(this@GeofenceListActivity, false)

                }
            })
    }

}

