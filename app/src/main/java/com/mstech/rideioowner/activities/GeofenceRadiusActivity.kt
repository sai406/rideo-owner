package com.mstech.rideioowner.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.blankj.utilcode.util.LogUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.mstech.rideioowner.R
import kotlinx.android.synthetic.main.activity_geofence.*

class GeofenceRadiusActivity : BaseActivity() , OnMapReadyCallback {
    var googleMap: GoogleMap? = null
    var latitude: Double = -28.016666
    var longitude: Double = 153.399994
    var midlatlng: LatLng = LatLng(0.0, 0.0)
    var imei = ""
    var radius = 0.0
    private var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofence_radius)
        setScreenTitle("Saved Geofence")
        getBackButton().setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        getHomeButton().setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        })
        try {
            radius = intent.getStringExtra("radius").replace("m","").toDouble()
            latitude = intent.getStringExtra("lat").toDouble()
            longitude = intent.getStringExtra("lng").toDouble()
            midlatlng = LatLng(latitude,longitude)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        val latLng = LatLng(latitude, longitude)
//        googleMap!!.addMarker(
//            MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker))
//                .anchor(0.0f, 1.0f)
//                .position(latLng)
//        )
        googleMap!!.uiSettings.isMyLocationButtonEnabled = false
        googleMap!!.uiSettings.isZoomControlsEnabled = true

        // Updates the location and zoom of the MapView
        // Updates the location and zoom of the MapView
        radiustxt.text = radius.toString() + "m"
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(midlatlng, 13f)
        googleMap!!.moveCamera(cameraUpdate)
        val circleOptions1 = CircleOptions()
            .center(midlatlng)
            .radius(radius).strokeColor(Color.BLACK)
            .strokeWidth(2f).fillColor(0x500000ff)
        googleMap!!.addCircle(circleOptions1)
    }
}
