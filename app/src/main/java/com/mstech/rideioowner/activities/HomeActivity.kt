package com.mstech.rideioowner.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.mstech.rideioowner.R
import kotlinx.android.synthetic.main.activity_home.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class HomeActivity : AppCompatActivity(), View.OnClickListener{


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        profile.setOnClickListener(this)
        alerts.setOnClickListener(this)
        command.setOnClickListener(this)
        geofence.setOnClickListener(this)
        tracking.setOnClickListener(this)
        trips.setOnClickListener(this)
        vehicle.setOnClickListener(this)
        driver.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
      when ( v?.id){
          R.id.profile ->{
              ToastUtils.showShort("Navigation")
          }R.id.alerts ->{
              ToastUtils.showShort("Alerts")
          }R.id.command ->{
              ToastUtils.showShort("Command")
          }R.id.geofence ->{
              ToastUtils.showShort("GeoFence")
          }R.id.tracking ->{
              ToastUtils.showShort("Tracking")
          }R.id.trips ->{
              ToastUtils.showShort("Playback")
          }R.id.driver ->{
              ToastUtils.showShort("History")
          }R.id.vehicle ->{
              ToastUtils.showShort("Vehicle")
          }
      }
    }
}
