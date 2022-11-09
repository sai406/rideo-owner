package com.mstech.rideioowner.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.mstech.rideioowner.R
import com.mstech.rideioowner.activities.GeofenceActivity
import com.mstech.rideioowner.activities.GeofenceRadiusActivity
import com.mstech.rideioowner.model.GeofenceListResponse
import com.mstech.rideioowner.utils.MyUtils
import com.mstech.rideioowner.utils.RetrofitFactory
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GeofenceListAdapter(
    var context: Context,
    samplelist: MutableList<GeofenceListResponse>
) :
    RecyclerView.Adapter<GeofenceListAdapter.MyViewHolder>() {
    private val samplelist: MutableList<GeofenceListResponse>
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.geo_list_item, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val m: GeofenceListResponse = samplelist[position] as GeofenceListResponse
        holder.geo_name.text=m.geoFenceName
        holder.radius.text = m.radius
        holder.time.text = m.createdDate
        holder.delete.setOnClickListener(View.OnClickListener {
            deleteGeofence(m.geoFenceId,position);
        })
        holder.navigation.setOnClickListener(View.OnClickListener {  context.startActivity(Intent(context, GeofenceRadiusActivity::class.java).putExtra("radius",m.radius).putExtra("lat",m.centerLatitude).putExtra("lng",m.centerLongitude))
        })
    }

    private fun deleteGeofence(geoFenceId: Int, param: Int) {
        MyUtils.showProgress(context, true)
        var obj = JSONObject()
        obj.put("GeofenceId", geoFenceId)
        LogUtils.e(obj.toString())
        var body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            ((obj)).toString()
        )
        RetrofitFactory.client
            .delete_geofence(
                body
            )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    MyUtils.showProgress(context, false)
                    if (response.isSuccessful
                    ) {
                        try {
                            LogUtils.e(response.body()?.string())
                            if (response.body()!!.string()!!.toString() != null) {
                                ToastUtils.showShort("Geofence Deleted Successfully")
                                removeAt(param)
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
                    MyUtils.showProgress(context, false)

                }
            })
    }

    override fun getItemCount(): Int {
        return samplelist.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var geo_name: TextView
        var radius: TextView
        var time: TextView
        var navigation: ImageView
        var delete: ImageView
        //
        init {
            // get the reference of item view's
            geo_name = itemView.findViewById<View>(R.id.geo_name) as TextView
            radius = itemView.findViewById<View>(R.id.radius) as TextView
            time = itemView.findViewById<View>(R.id.time) as TextView
            navigation = itemView.findViewById<View>(R.id.navigation) as ImageView
            delete = itemView.findViewById<View>(R.id.delete) as ImageView
        }
    }

    init {
        this.samplelist = samplelist
    }

    fun removeAt(position: Int) {
        samplelist.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, samplelist?.size)
    }
}