package com.college.collegeconnect.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.database.MondayEntity
import com.college.collegeconnect.database.SubjectDetails
import com.college.collegeconnect.datamodels.DatabaseHelper
import com.college.collegeconnect.datamodels.SaveSharedPreference
import com.college.collegeconnect.models.AttendanceViewModel
import com.github.lzyzsd.circleprogress.ArcProgress
import java.util.*
import kotlin.collections.ArrayList

class TimetableAdapter(private val subjects: ArrayList<MondayEntity>, private val context: Context) : RecyclerView.Adapter<TimetableAdapter.ViewHolder>() {
    var per = 0
    var criteria = 0f
    var predict = 0f
    var attended: Int = 0
    var missed: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.timetable_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.heading.text = subjects[position].subjectName
        holder.start.text = subjects[position].startTime
        holder.end.text = subjects[position].endTime
//        criteria = SaveSharedPreference.getAttendanceCriteria(context).toFloat()
//        val current = subjects[position].subjectName
//        holder.circleProgress.max = 100
//        holder.circleProgress.progress = 25
//        //Setting details of cards on loading
//        holder.heading.text = current
//        attended = subjects[position].attended
//        missed = subjects[position].missed
//        holder.ratio.text = "$attended/${missed + attended}"
//        val percentage = String.format("%.0f", attended.toFloat() / (attended + missed) * 100)
//        per = if (percentage == "NaN") 0 else percentage.toFloat().toInt()
//        holder.circleProgress.progress = per
//        predict = attended.toFloat() / (attended + missed + 1) * 100
//        var i: Int
//        if (predict < criteria && percentage != "NaN") {
//            holder.tv_bunk.text = "You can\'t miss any more lectures"
//        } else {
//            if (percentage == "NaN") holder.tv_bunk.text = "No classes have happened yet" else {
//                i = 1
//                if (attended.toFloat() / (attended + missed + 2) * 100 >= criteria)
//                    i = 2
//                if (attended.toFloat() / (attended + missed + 3) * 100 >= criteria)
//                    i = 3
//                if (attended.toFloat() / (attended + missed + 4) * 100 >= criteria)
//                    i = 4
//                if (i == 4)
//                    holder.tv_bunk.text = "You can miss more than 3 lectures"
//                else holder.tv_bunk.text = "You can miss $i lecture(s)"
//            }
//        }
//        holder.circleProgress.progress = per
//
//        //Button functionality
//        holder.increase.setOnClickListener {
//            attended = subjects[position].attended
//            missed = subjects[position].missed
//            val sub = SubjectDetails(subjects[position].subjectName, attended+1, missed)
//            sub.id = subjects[position].id
//            viewModel.updateSubject(sub)
//            notifyDataSetChanged()
//        }
//        holder.decrease.setOnClickListener {
//            attended = subjects[position].attended
//            missed = subjects[position].missed
//            val sub = SubjectDetails(subjects[position].subjectName, attended, missed+1)
//            sub.id = subjects[position].id
//            viewModel.updateSubject(sub)
//            notifyDataSetChanged()
//        }
//        holder.delete.setOnClickListener { view ->
//            val popup = PopupMenu(context, view)
//            val inflater = popup.menuInflater
//            inflater.inflate(R.menu.actions, popup.menu)
//            popup.setOnMenuItemClickListener { menuItem ->
//                when (menuItem.itemId) {
//                    R.id.delete -> {
//                        subjects[position].id.let { viewModel.delete(it) }
//                            notifyDataSetChanged()
//                    }
//                }
//                true
//            }
//            popup.show()
//        }
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var heading: TextView = itemView.findViewById(R.id.subjectTitle)
        var start: TextView = itemView.findViewById(R.id.begin)
        var end: TextView = itemView.findViewById(R.id.end)

    }
}