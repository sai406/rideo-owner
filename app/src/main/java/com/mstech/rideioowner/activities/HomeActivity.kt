package com.mstech.rideioowner.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.rideioowner.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), View.OnClickListener{

    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setScreenTitle("Rideio")
        getBackButton().visibility= View.GONE
        getHomeButton().visibility =View.GONE
        getLogoutButton().visibility =View.VISIBLE
        getLogoutButton().setOnClickListener(View.OnClickListener {    val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage("Do you want to Logout ?")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                        dialog, id ->
                    SPStaticUtils.clear()
                    startActivity(Intent(this,LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                })
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Trip End")
            // show alert dialog
            alert.show()

        })

        profile.setOnClickListener(this)
        alerts.setOnClickListener(this)
        command.setOnClickListener(this)
        geofence.setOnClickListener(this)
        tracking.setOnClickListener(this)
        trips.setOnClickListener(this)
        vehicle.setOnClickListener(this)
        driver.setOnClickListener(this)
    }
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
    override fun onClick(v: View?) {
      when ( v?.id){
          R.id.profile ->{
              startActivity(Intent(this,ProfileActivity::class.java))
          }R.id.alerts ->{
          startActivity(Intent(this,AlertsActivity::class.java))
          }R.id.command ->{
              ToastUtils.showShort("Command")
          }R.id.geofence ->{
              ToastUtils.showShort("GeoFence")
          }R.id.tracking ->{
             startActivity(Intent(this,LiveTrackActivity::class.java))
          }R.id.trips ->{
          startActivity(Intent(this,TripsActivity::class.java))
          }R.id.driver ->{
          startActivity(Intent(this,DriverListActivity::class.java))
          }R.id.vehicle ->{
          startActivity(Intent(this,VehiclesList::class.java))
          }
      }
    }
}
