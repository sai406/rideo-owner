package com.mstech.rideioowner.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mstech.rideioowner.R
import com.mstech.rideioowner.activities.GeofenceActivity
import com.mstech.rideioowner.activities.ShowAlertsMapActivity
import com.mstech.rideioowner.model.VehicleListResponse

class VehicleListAdapter(
    var context: Context, samplelist: MutableList<VehicleListResponse>, b: Boolean
) : RecyclerView.Adapter<VehicleListAdapter.MyViewHolder>() {
    private val samplelist: MutableList<VehicleListResponse>
    private val b: Boolean
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.alerts_list_item, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val m: VehicleListResponse = samplelist[position] as VehicleListResponse
        holder.vehicle_number.text = m.vehicleNumber
        holder.event_type.text = m.deviceName
        holder.time.text = m.imei
        holder.address.text = m.address
        holder.navigation.setOnClickListener(View.OnClickListener { v ->
            if (b == true) {
                context.startActivity(Intent(context, GeofenceActivity::class.java).putExtra("imei",m.imei))
            } else {
                val i = Intent(context, ShowAlertsMapActivity::class.java)
                i.putExtra("lat", m.latitude)
                i.putExtra("lon", m.longitude)
                context.startActivity(i)
            }

        }
        )

    }

    override fun getItemCount(): Int {
        return samplelist.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var vehicle_number: TextView
        var event_type: TextView
        var time: TextView
        var address: TextView
        var navigation: RelativeLayout

        //
        init {
            // get the reference of item view's
            vehicle_number = itemView.findViewById<View>(R.id.vehicle_number) as TextView
            event_type = itemView.findViewById<View>(R.id.event_type) as TextView
            time = itemView.findViewById<View>(R.id.time) as TextView
            address = itemView.findViewById<View>(R.id.address) as TextView
            navigation = itemView.findViewById<View>(R.id.navigation) as RelativeLayout
        }
    }

    init {
        this.samplelist = samplelist
        this.b = b
    }
}