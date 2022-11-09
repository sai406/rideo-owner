package com.mstech.rideiodriver.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mstech.rideiodriver.Model.TripHistory
import com.mstech.rideioowner.R
import com.mstech.rideioowner.activities.ShowAlertsMapActivity

class PlaybackAlertsAdapter(
    var context: Context,
    samplelist: MutableList<TripHistory>
) :
    RecyclerView.Adapter<PlaybackAlertsAdapter.MyViewHolder>() {
    private val samplelist: MutableList<TripHistory>
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
        val m: TripHistory = samplelist[position] as TripHistory
        holder.vehicle_number.text= m.vehicleNumber?.toString()
        holder.event_type.text = m.event?.toString()
        holder.time.text = m.dataDay?.toString()
        holder.address.text = m.address?.toString()
        holder.navigation.setOnClickListener(View.OnClickListener { v ->
            val i = Intent(context, ShowAlertsMapActivity::class.java)
            i.putExtra("lat",m.latitude)
            i.putExtra("lon",m.longitude)
            context.startActivity(i)
        })
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