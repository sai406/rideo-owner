package com.mstech.rideioowner.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.rideiodriver.Adapter.PlaybackAlertsAdapter
import com.mstech.rideiodriver.Model.PlaybackAlerts
import com.mstech.rideiodriver.Model.TripHistory
import com.mstech.rideioowner.R
import com.mstech.rideioowner.activities.BaseActivity
import com.mstech.rideioowner.activities.HomeActivity
import com.mstech.rideioowner.utils.MyUtils
import com.mstech.rideioowner.utils.RetrofitFactory
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TripAlertsActivity : BaseActivity() {
    lateinit var recyclerView: RecyclerView
    var tripid: String? = null
    private var triphistory: MutableList<TripHistory>? = null
    private var finaltriphistory: MutableList<TripHistory>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_alerts)
        setScreenTitle("Alerts")
        getBackButton().setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        getHomeButton().setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        })
        recyclerView = findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = linearLayoutManager
        var data = intent.extras
        if (data != null) {
            tripid = data.getString("tripid")
        }else{
            tripid = "0"
        }
        LogUtils.e(tripid)
        if(NetworkUtils.isConnected()){
            getAlertsList()
        }else{
            ToastUtils.showShort("No Internet Connection")
        }
    }
    private fun getAlertsList() {
        MyUtils.showProgress(this@TripAlertsActivity, true)
        var obj = JSONObject()
        obj.put("TripId", tripid)
        var body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            ((obj)).toString()
        );
        RetrofitFactory.client
            .getTripHistory(
                body
            )
            .enqueue(object : Callback<PlaybackAlerts> {
                override fun onResponse(
                    call: Call<PlaybackAlerts>,
                    response: Response<PlaybackAlerts>
                ) {
                    MyUtils.showProgress(this@TripAlertsActivity, false)
                    if (response.isSuccessful
                    ) {
                        try {
                           var  data =(response.body())
                             triphistory = (data?.tripHistory)
                            for (i in triphistory?.size!!-1  downTo  0) {
                                if (triphistory!![i].packetTypeId.equals("0")) {
                                    triphistory!!.removeAt(i)
                                }
                            }
                            var adapter: PlaybackAlertsAdapter? =
                                triphistory?.let { PlaybackAlertsAdapter(this@TripAlertsActivity, it) }
                            recyclerView?.setAdapter(adapter)
                            adapter?.notifyDataSetChanged()
                            if(triphistory?.size!!<1){
                                ToastUtils.showShort("No Alerts Found")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                override fun onFailure(
                    call: Call<PlaybackAlerts>,
                    t: Throwable
                ) {
                    ToastUtils.showShort(t.message)
                    MyUtils.showProgress(this@TripAlertsActivity, false)

                }
            })
    }

}
