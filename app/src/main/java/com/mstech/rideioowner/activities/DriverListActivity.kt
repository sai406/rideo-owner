package com.mstech.rideioowner.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.rideioowner.R
import com.mstech.rideioowner.adapter.DriverListAdapter
import com.mstech.rideioowner.adapter.VehicleListAdapter
import com.mstech.rideioowner.model.DriverListResponse
import com.mstech.rideioowner.model.SharedKey
import com.mstech.rideioowner.model.VehicleListResponse
import com.mstech.rideioowner.utils.MyUtils
import com.mstech.rideioowner.utils.RetrofitFactory
import kotlinx.android.synthetic.main.activity_vehicles_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class DriverListActivity : BaseActivity() {
    var devicelist: List<DriverListResponse> =
        ArrayList<DriverListResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_list)
        setScreenTitle("Drivers")
        getBackButton().setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        getHomeButton().setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        })
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = linearLayoutManager
        if(NetworkUtils.isConnected()){
            getOwnerVehiclesList()
        }else{
            ToastUtils.showShort("No Internet Connection")
        }
    }

    private fun getOwnerVehiclesList() {
        MyUtils.showProgress(this, true)

        RetrofitFactory.client
            .getDriverList(SPStaticUtils.getString(SharedKey.OWNER_ID, ""))
            ?.enqueue(object : Callback<List<DriverListResponse?>> {
                override fun onResponse(
                    call: Call<List<DriverListResponse?>>,
                    response: Response<List<DriverListResponse?>>
                ) {
                    MyUtils.showProgress(this@DriverListActivity, false)

                    if (response.isSuccessful) {
                        Log.d("", "onResponse: " + response.body())
                        devicelist = response.body() as List<DriverListResponse>
                        var adapter: DriverListAdapter? =
                            devicelist?.let { DriverListAdapter(this@DriverListActivity,
                                it as MutableList<DriverListResponse>
                            ) }
                        recyclerView?.adapter = adapter
                        adapter?.notifyDataSetChanged()
                        if (devicelist!!.size > 0) {
                        } else {
                            ToastUtils.showShort("No Vehicles Found")
                        }


                    } else {
                        ToastUtils.showShort(response.code())
                    }
                }

                override fun onFailure(
                    call: Call<List<DriverListResponse?>>,
                    t: Throwable
                ) {
                    ToastUtils.showShort(t.message)
                    MyUtils.showProgress(this@DriverListActivity, false)

                }
            })
    }
}
