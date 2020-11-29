package com.college.collegeconnect.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.database.entity.FridayEntity
import com.college.collegeconnect.database.entity.TimetableEntity
import com.college.collegeconnect.timetable.NewTimeTableViewModel
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

class TimetableAdapter(
        private val subjects: ArrayList<TimetableEntity>,
        private val context: Context,
        private var newTimeTableViewModel: NewTimeTableViewModel,
        private val dayOfWeek: Int) : RecyclerView.Adapter<TimetableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.timetable_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == subjects.size-1)
            holder.bottomDivider.visibility = View.VISIBLE
        holder.heading.text = subjects[position].subjectName
        holder.time.text = "${subjects[position].startTimeShow} - ${subjects[position].endTimeShow}"
        holder.roomNumber.text = subjects[position].roomNumber
        val pattern = "dd-MM-yyyy"
        val dateInString: String = SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
//        val localDate = LocalDate.of(dateInString.substring(6,10).toInt(), dateInString.substring(3,5).toInt(), dateInString.substring(0,2).toInt())
//        val day = DayOfWeek.from(localDate).value
//        val cal = Calendar.getInstance()
//        cal.firstDayOfWeek = Calendar.MONDAY
        val startingTime = getMilli("$dateInString ${subjects[position].startTime}")
        val endingTime = getMilli("$dateInString ${subjects[position].endTime}")
        Log.d("TimeTableAdapter", "${dayOfWeek + 2} Calendar = ${Calendar.getInstance()[Calendar.DAY_OF_WEEK]}")

        if(System.currentTimeMillis() in startingTime until endingTime) {
            // for monday to saturday
            if (dayOfWeek + 2 == Calendar.getInstance()[Calendar.DAY_OF_WEEK]) {
                holder.state.visibility = View.VISIBLE
                holder.stateCircle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ellipse_filled))
            }
            // for sunday
            if (dayOfWeek == 6 && Calendar.getInstance()[Calendar.DAY_OF_WEEK]==1){
                holder.state.visibility = View.VISIBLE
                holder.stateCircle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ellipse_filled))
            }
        }
        else{
            holder.state.visibility = View.INVISIBLE
            holder.stateCircle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ellipse_hollow))
        }

        //Delete Class
        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = (context as AppCompatActivity).layoutInflater
            val view = inflater.inflate(R.layout.layout_delete_timetable_class, null)
            view.findViewById<TextView>(R.id.txt_sub_name).text = subjects[position].subjectName
            view.findViewById<TextView>(R.id.txt_time_schedule).text = "${subjects[position].startTimeShow} - ${subjects[position].endTimeShow}"
            view.findViewById<TextView>(R.id.txt_day).text = SectionsPagerAdapter.TAB_TITLES[dayOfWeek]
            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            view.findViewById<Button>(R.id.btn_delete_class).setOnClickListener {
                Log.d("TimeTableAdapter", "Delete: $dayOfWeek")
                newTimeTableViewModel.deleteClass(dayOfWeek, subjects[position])
                dialog.dismiss()
            }
            dialog.show()
        true
        }
    }

    // get time in milliseconds
    private fun getMilli(myDate: String): Long {
        val format = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
        val d = format.parse(myDate)
        return d.time
    }

    override fun getItemCount() = subjects.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var heading: TextView = itemView.findViewById(R.id.subject_title)
        var time: TextView = itemView.findViewById(R.id.time)
        var state: TextView = itemView.findViewById(R.id.lec_state)
        var stateCircle: ImageView = itemView.findViewById(R.id.lec_state_circle)
        var roomNumber: TextView = itemView.findViewById(R.id.room_num)
        var bottomDivider = itemView.findViewById<View>(R.id.divider_bottom)
    }
}