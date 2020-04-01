package com.mstech.rideioowner.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import com.mstech.rideioowner.R
import com.mstech.rideioowner.model.SharedKey
import com.mstech.rideioowner.utils.MyUtils
import com.mstech.rideioowner.utils.RetrofitFactory
import kotlinx.android.synthetic.main.activity_geofence.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GeofenceActivity : BaseActivity(), OnMapReadyCallback {
    var googleMap: GoogleMap? = null
    var latitude: Double = -28.016666
    var longitude: Double = 153.399994
    var midlatlng: LatLng = LatLng(0.0, 0.0)
    var imei = ""
    var simpleSeekBar: SeekBar? = null
    private var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofence)
        setScreenTitle("Geofence")
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
            imei = intent.getStringExtra("imei")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        simpleSeekBar = findViewById<View>(R.id.simpleSeekBar) as SeekBar
        simpleSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var progressChangedValue = 0
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                progressChangedValue = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) { // TODO Auto-generated method stub
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

                index = progressChangedValue
                radiustxt.text = index.toDouble().toString() + "m"
                googleMap!!.clear()
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(midlatlng, 14f)
                googleMap!!.moveCamera(cameraUpdate)
                val circleOptions1 = CircleOptions()
                    .center(midlatlng)
                    .radius(index.toDouble()).strokeColor(Color.BLACK)
                    .strokeWidth(2f).fillColor(0x500000ff)
                googleMap!!.addCircle(circleOptions1)

            }
        })
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        save.setOnClickListener(View.OnClickListener {

            MyUtils.showProgress(this, true)
            var obj = JSONObject()
            obj.put("Imei", imei)
            obj.put("OwnerId", SPStaticUtils.getString(SharedKey.OWNER_ID, ""))
            obj.put("Radius", index)
            obj.put("CenterLatitude", midlatlng.latitude)
            obj.put("CenterLongitude", midlatlng.longitude)
            LogUtils.e(obj.toString())
            var body = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                ((obj)).toString()
            )
            RetrofitFactory.client
                .setGeofence(
                    body
                )
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        MyUtils.showProgress(this@GeofenceActivity, false)
                        if (response.isSuccessful
                        ) {
                            try {
                                LogUtils.e(response.body()?.string())
                                if (response.body()!!.string()!!.toString() != null) {
                                    ToastUtils.showShort("Geofence Added Successfull")
                                    onBackPressed()
                                }else{
                                    ToastUtils.showShort("Please try again")
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseBody>,
                        t: Throwable
                    ) {
                        ToastUtils.showShort(t.message)
                        MyUtils.showProgress(this@GeofenceActivity, false)

                    }
                })
        })

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
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
        googleMap!!.moveCamera(cameraUpdate)
        googleMap!!.setOnCameraIdleListener(GoogleMap.OnCameraIdleListener {
            midlatlng = googleMap!!.getCameraPosition().target
            LogUtils.e(midlatlng)
        })
    }
}
