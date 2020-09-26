package com.college.collegeconnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.datamodels.Events
import com.college.collegeconnect.utils.toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BvestEventsAdapter(val context: Context, val arrayList: ArrayList<Events>):RecyclerView.Adapter<BvestEventsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_grid_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = arrayList[position]
        holder.textView.text = event.eventName
        if (event.organizer.toLowerCase(Locale.ROOT).contains("dsc"))
            holder.imageView!!.setImageDrawable(context.getDrawable(R.drawable.dsc))
        else if (event.organizer.toLowerCase(Locale.ROOT).contains("ieee"))
            holder.imageView!!.setImageDrawable(context.getDrawable(R.drawable.bvpieee))
        else if (event.organizer.toLowerCase(Locale.ROOT).contains("csi"))
            holder.imageView!!.setImageDrawable(context.getDrawable(R.drawable.bvpcsi))

        holder.date.text = date(event.date)
        holder.itemView.setOnClickListener {
            context.toast(event.eventName)
        }
    }

    override fun getItemCount() = arrayList.size

    private fun date(date: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
        val datetext: Date
        var str: String? = null
        try {
            datetext = inputFormat.parse(date)
            str = outputFormat.format(datetext)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.ivEventBannerCardImage)
        val textView = itemView.findViewById<TextView>(R.id.tvEventTitle)
        val date = itemView.findViewById<TextView>(R.id.tvEventDate)
    }
}