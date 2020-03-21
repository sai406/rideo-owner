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
import com.mstech.rideioowner.activities.ShowAlertsMapActivity
import com.mstech.rideioowner.model.DriverListResponse

class DriverListAdapter(
    var context: Context,
    samplelist: MutableList<DriverListResponse>
) :
    RecyclerView.Adapter<DriverListAdapter.MyViewHolder>() {
    private val samplelist: MutableList<DriverListResponse>
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
        val m: DriverListResponse = samplelist[position] as DriverListResponse
        holder.vehicle_number.text=m.FirstName+m.LastName
        holder.event_type.text = m.Email
        holder.time.text = m.Mobile
        holder.address.text = m.Address
        holder.navigation.visibility = View.GONE
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
    }
}