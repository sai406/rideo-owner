package com.mstech.rideioowner.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mstech.rideiodriver.Adapter.GetTripsAdapter
import com.mstech.rideiodriver.Model.GetTripsResponse
import com.mstech.rideioowner.R
import com.mstech.rideioowner.model.SharedKey
import com.mstech.rideioowner.model.VehicleListResponse
import com.mstech.rideioowner.utils.AppConstants
import com.mstech.rideioowner.utils.MyUtils
import com.mstech.rideioowner.utils.RetrofitFactory
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.UnsupportedEncodingException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TripsActivity : BaseActivity() {
    lateinit var txtDate: TextView
    lateinit var txtTime: TextView
    lateinit var filter: FloatingActionButton
     var txt1: TextView? = null
     var txt2: TextView? = null
    lateinit var vehicle: Spinner
    var starttime: Long = 0
    var endtime: Long = 0
    var totaltime: Long = 0
    var value: Long = 0
    var finalstarttime: Any = 0
    var finalendtime: Any = 0
    private var mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    var adapter: GetTripsAdapter? = null
    var recyclerView: RecyclerView? = null
    var devicelist: List<VehicleListResponse> =
        ArrayList<VehicleListResponse>()
    var vehiclelist: MutableList<String> = ArrayList()
    lateinit var adapter1: ArrayAdapter<String>
    lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trips)
        setScreenTitle("Trip History")
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
        getCurrentWeekDate(-1)
        filter = findViewById(R.id.filter)
        recyclerView = findViewById(R.id.recyclerview)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = linearLayoutManager
        val gsonBuilder: GsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat("M/d/yy hh:mm a")
        gson = gsonBuilder.create()
        if (NetworkUtils.isConnected()) {
            getOwnerVehiclesList()
        } else {
            ToastUtils.showShort("No Internet Connection")
        }

        filter.setOnClickListener(View.OnClickListener { v ->
            showCustomDialog()
        })
    }

    fun showCustomDialog() {
        val viewGroup: ViewGroup = this.findViewById(android.R.id.content)
        var dialogView: View =
            LayoutInflater.from(this).inflate(R.layout.date_custom_dialog, viewGroup, false)
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        var alertDialog: AlertDialog = builder.create()
        var back: ColorDrawable = ColorDrawable(Color.WHITE)
        var inset: InsetDrawable = InsetDrawable(back, 20)
        alertDialog.getWindow()!!.setBackgroundDrawable(inset)
        txtDate = dialogView.findViewById(R.id.txtDate)
        txtTime = dialogView.findViewById(R.id.txtTime)
        txt2 = dialogView.findViewById(R.id.txt2)
        txt1 = dialogView.findViewById(R.id.txt1)
        vehicle = dialogView.findViewById(R.id.vehicle)
        vehicle.setAdapter(adapter1)
        var ok_btn: MaterialButton = dialogView.findViewById(R.id.ok)
        var cancel_btn: MaterialButton = dialogView.findViewById(R.id.cancel)
        var thisweek: TextView = dialogView.findViewById(R.id.thisweek)
        var lastweek: TextView = dialogView.findViewById(R.id.lastweek)
        var today: TextView = dialogView.findViewById(R.id.today)
        var yesterday: TextView = dialogView.findViewById(R.id.yesterday)
        thisweek.setOnClickListener(View.OnClickListener { v ->
            yesterday.setBackgroundResource(R.color.white);
            today.setBackgroundResource(R.color.white);
            thisweek.setBackgroundResource(R.color.colorPrimary);
            lastweek.setBackgroundResource(R.color.white);
            getCurrentWeekDate(-1)
        })
        ok_btn.setOnClickListener(View.OnClickListener { v ->
            if (txt1?.text!!.length > 1 && txt2?.text!!.length < 1 || txt1?.text!!.length < 1 && txt2?.text!!.length > 1||txt1?.text!!.length < 1 && txt2?.text!!.length < 1) {
                ToastUtils.showShort("Select Start and End Dates")
            } else {
                alertDialog.dismiss()
                getTrips()
            }
        })
        yesterday.setBackgroundResource(R.color.white);
        today.setBackgroundResource(R.color.colorPrimary);
        lastweek.setBackgroundResource(R.color.white);
        thisweek.setBackgroundResource(R.color.white);
        var date: Date = Date()
        getStartOfDay(date)
        today.setOnClickListener(View.OnClickListener { v ->
            yesterday.setBackgroundResource(R.color.white);
            today.setBackgroundResource(R.color.colorPrimary);
            lastweek.setBackgroundResource(R.color.white);
            thisweek.setBackgroundResource(R.color.white);
            var date: Date = Date()
            getStartOfDay(date)
        })
        yesterday.setOnClickListener(View.OnClickListener { v ->
            yesterday.setBackgroundResource(R.color.colorPrimary);
            today.setBackgroundResource(R.color.white);
            lastweek.setBackgroundResource(R.color.white);
            thisweek.setBackgroundResource(R.color.white);
            var cal: Calendar = Calendar.getInstance()
            cal.add(Calendar.DATE, -1);
            getStartOfDay(cal.time)
        })
        cancel_btn.setOnClickListener(View.OnClickListener { v ->
            alertDialog.dismiss()
        })
        lastweek.setOnClickListener(View.OnClickListener { v ->
            yesterday.setBackgroundResource(R.color.white);
            today.setBackgroundResource(R.color.white);
            lastweek.setBackgroundResource(R.color.colorPrimary);
            thisweek.setBackgroundResource(R.color.white);
            getCurrentWeekDate(-7)
        })
        txtDate.setOnClickListener(View.OnClickListener { v ->
            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    txt1!!.text = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    val str_date = txt1!!.text.toString()
                    val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                    try {
                        val date: Date = formatter.parse(str_date) as Date
                        starttime = date.time
                        totaltime = endtime - starttime
                        value = 604800000
                        if (endtime != 0L) {
                            if (starttime < endtime) {
                                if (totaltime < value) {

                                } else {
                                    Toast.makeText(
                                        this,
                                        "Select Date\nWith in 7Days ",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Enter correct\n date",
                                    Toast.LENGTH_LONG
                                ).show()
                                txt2!!.text = ""
                            }
                        }

                        //
                        LogUtils.d("", "onDateSet:" + date.time)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                }, mYear, mMonth, mDay
            )

            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.datePicker.minDate = mDay.toLong()
            datePickerDialog.show()

        })
        txtTime.setOnClickListener(View.OnClickListener { v ->
            val c = Calendar.getInstance()
            mYear = c[Calendar.YEAR]
            mMonth = c[Calendar.MONTH]
            mDay = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    txt2!!.text = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    val str_date = txt2!!.text.toString()
                    val formatter: DateFormat =
                        SimpleDateFormat("dd-MM-yyyy")
                    try {
                        val date = formatter.parse(str_date) as Date
                        endtime = date.time
                        totaltime = endtime - starttime
                        value = 604800000
                        if (starttime < endtime) {
                            if (totaltime < value) {
                            } else {
                                Toast.makeText(
                                    this,
                                    "Select Date\nWith in 7Days ",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Select correct\n date",
                                Toast.LENGTH_LONG
                            ).show()
                            txt2!!.text = ""
                        }


                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        })

        alertDialog.show()
    }

    fun getCurrentWeekDate(week: Int) {
        var c: Calendar = GregorianCalendar.getInstance()
        c.setFirstDayOfWeek(Calendar.MONDAY)
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek())
        c.add(Calendar.DAY_OF_WEEK, week)
        var df: DateFormat = SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.getDefault())
        var startDate: String
        var endDate: String
        startDate = df.format(c.getTime())
        c.add(Calendar.DAY_OF_MONTH, 6)
        endDate = df.format(c.getTime())
        finalstarttime = startDate
        finalendtime = endDate
        txt1?.text = startDate
        txt2?.text = endDate
        System.out.println("Start Date = " + startDate)
        System.out.println("End Date = " + endDate)
    }

    fun getStartOfDay(date: Date) {
        val formatter: DateFormat =
            SimpleDateFormat("MM-dd-yyyy hh:mm a")
        var calendar: Calendar = Calendar.getInstance()
        calendar.setTime(date)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        finalstarttime = formatter.format(calendar.time)
        txt1?.text = finalstarttime.toString()
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        finalendtime = formatter.format(calendar.time)
        txt2?.text = finalendtime.toString()
        System.out.println("Start Date = " + finalstarttime)
        System.out.println("End Date = " + finalendtime)
    }

    fun getTrips() {
        val formatter: DateFormat =
            SimpleDateFormat("MM-dd-yyyy hh:mm a")
        if (starttime != endtime) {
            finalstarttime = formatter.format(starttime)
            finalendtime = formatter.format(endtime)
        }
        var imei = "0"
        if (::vehicle.isInitialized) {
            imei = vehicle.selectedItem.toString()
        }
        LogUtils.d("", "onDateSet:" + finalstarttime + ",,," + finalendtime)
        var params = JSONObject()
        params.put("OwnerId", SPStaticUtils.getString(SharedKey.OWNER_ID, ""))
        params.put("DriverId", SPStaticUtils.getString(SharedKey.DRIVER_ID, ""))
        params.put("FromTime", finalstarttime.toString())
        params.put("ToTime", finalendtime.toString())
        params.put("Imei", imei)
        Log.d("", "params " + params.toString())
        MyUtils.showProgress(this, true)
        var mRequestBody = params.toString()
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST,
            AppConstants.GET_TRIPS,
            com.android.volley.Response.Listener { response ->
                Log.d("response", response)
                MyUtils.showProgress(this, false)
                var tripslist: MutableList<GetTripsResponse> = mutableListOf()
                try {
                    val gson = Gson()
                    val arrayTutorialType =
                        object : TypeToken<MutableList<GetTripsResponse>>() {}.type
                    tripslist.clear()
                    tripslist = gson.fromJson(response, arrayTutorialType)
                    adapter = GetTripsAdapter(this, tripslist)
                    recyclerView!!.adapter = adapter
                    if (tripslist.size > 0) {
                    } else {
                        ToastUtils.showShort("No Trips Found")
                    }
                    adapter?.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            com.android.volley.Response.ErrorListener { error ->
                Log.e("LOG_VOLLEY", error.toString())
                MyUtils.showProgress(this, false)
                ToastUtils.showShort(error.toString())

            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                return try {
                    mRequestBody.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    VolleyLog.wtf(
                        "Unsupported Encoding while trying to get the bytes of %s using %s",
                        mRequestBody,
                        "utf-8"
                    )
                    null
                }
            }
        }
        requestQueue.add(stringRequest)


    }

    private fun getOwnerVehiclesList() {
        MyUtils.showProgress(this, true)
        RetrofitFactory.client
            .getOwnerVehiclelist(SPStaticUtils.getString(SharedKey.OWNER_ID, ""))
            ?.enqueue(object : Callback<List<VehicleListResponse?>> {
                override fun onResponse(
                    call: Call<List<VehicleListResponse?>>,
                    response: Response<List<VehicleListResponse?>>
                ) {
                    MyUtils.showProgress(this@TripsActivity, false)
                    if (response.isSuccessful) {
                        vehiclelist.clear()
                        Log.d("", "onResponse: " + response.body())
                        devicelist = response.body() as List<VehicleListResponse>
                        for (i in 0 until devicelist.size) {
                            vehiclelist.add(
                                devicelist.get(i).vehicleNumber
                            )
                        }
                        adapter1 = ArrayAdapter<String>(
                            this@TripsActivity,
                            android.R.layout.select_dialog_item,
                            vehiclelist
                        )
                        getTrips()
                    } else {
                        ToastUtils.showShort(response.code())
                    }
                }

                override fun onFailure(
                    call: Call<List<VehicleListResponse?>>,
                    t: Throwable
                ) {
                    MyUtils.showProgress(this@TripsActivity, false)
                    ToastUtils.showShort(t.message)

                }
            })
    }


}
