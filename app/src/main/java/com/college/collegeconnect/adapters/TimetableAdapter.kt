package com.college.collegeconnect.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.database.entity.TimetableEntity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TimetableAdapter(private val subjects: ArrayList<TimetableEntity>, private val context: Context) : RecyclerView.Adapter<TimetableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.timetable_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.heading.text = subjects[position].subjectName
        holder.time.text = "${subjects[position].startTimeShow} - ${subjects[position].endTimeShow}"
        val pattern = "yyyy-MM-dd"
        val dateInString: String = SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
        val startingTime = getMilli("$dateInString ${subjects[position].startTime}")
        val endingTime = getMilli("$dateInString ${subjects[position].endTime}")

        Log.d("TimetableAdapter", "onBindViewHolder: starting time: $startingTime")
        Log.d("TimetableAdapter", "onBindViewHolder: ending time: $endingTime")
        Log.d("TimetableAdapter", "onBindViewHolder: current time: ${System.currentTimeMillis()}")


        if(System.currentTimeMillis() in startingTime until endingTime){
            holder.state.visibility = View.VISIBLE
            holder.stateCircle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ellipse_filled))
        }
        else{
            holder.state.visibility = View.INVISIBLE
            holder.stateCircle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ellipse_hollow))
        }
    }

    private fun getMilli(myDate: String): Long {
        val format = SimpleDateFormat("yyyy-dd-MM hh:mm:ss",Locale.getDefault())
//        format.timeZone = TimeZone.getTimeZone("UTC")
        val d = format.parse(myDate)
        return d.time
    }

    override fun getItemCount() = subjects.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var heading: TextView = itemView.findViewById(R.id.subject_title)
        var time: TextView = itemView.findViewById(R.id.time)
        var state: TextView = itemView.findViewById(R.id.lec_state)
        var stateCircle: ImageView = itemView.findViewById(R.id.lec_state_circle)
    }
}