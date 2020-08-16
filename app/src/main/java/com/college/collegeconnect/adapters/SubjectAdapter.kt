package com.college.collegeconnect.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.database.SubjectDetails
import com.college.collegeconnect.datamodels.DatabaseHelper
import com.college.collegeconnect.datamodels.SaveSharedPreference
import com.college.collegeconnect.models.AttendanceViewModel
import com.college.collegeconnect.ui.attendance.AttendanceFragment
import com.github.lzyzsd.circleprogress.ArcProgress
import kotlin.collections.ArrayList

class SubjectAdapter(private val subjects: ArrayList<SubjectDetails?>, private val context: Context, private val viewModel: AttendanceViewModel) : RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {
    var per = 0
    var criteria = 0f
    var predict = 0f
    var attended: Int = 0
    var missed: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subject_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        criteria = SaveSharedPreference.getAttendanceCriteria(context).toFloat()
        val current = subjects[position]?.subjectName
        holder.circleProgress.max = 100
        holder.circleProgress.progress = 25
        //Setting details of cards on loading
        holder.heading.text = current
        attended = subjects[position]?.attended!!
        missed = subjects[position]?.missed!!
        holder.ratio.text = "$attended/${missed + attended}"
        val percentage = String.format("%.0f", attended.toFloat() / (attended + missed) * 100)
        per = if (percentage == "NaN") 0 else percentage.toFloat().toInt()
        holder.circleProgress.progress = per
        predict = attended.toFloat() / (attended + missed + 1) * 100
        var i: Int
        if (predict < criteria && percentage != "NaN") {
            holder.tv_bunk.text = "You can\'t miss any more lectures"
        } else {
            if (percentage == "NaN") holder.tv_bunk.text = "No classes have happened yet" else {
                i = 1
                if (attended.toFloat() / (attended + missed + 2) * 100 >= criteria)
                    i = 2
                if (attended.toFloat() / (attended + missed + 3) * 100 >= criteria)
                    i = 3
                if (attended.toFloat() / (attended + missed + 4) * 100 >= criteria)
                    i = 4
                if (i == 4)
                    holder.tv_bunk.text = "You can miss more than 3 lectures"
                else holder.tv_bunk.text = "You can miss $i lecture(s)"
            }
        }
        holder.circleProgress.progress = per

        //Button functionality
        holder.increase.setOnClickListener {
            attended++
            val sub = subjects[position]?.subjectName?.let { it1 -> SubjectDetails(it1, attended , missed) }
            sub?.id = subjects[position]?.id!!
            if (sub != null) {
                viewModel.updateSubject(sub)
            }
        }
        holder.decrease.setOnClickListener {
            missed++
            val sub = subjects[position]?.subjectName?.let { it1 -> SubjectDetails(it1, attended, missed ) }
            sub?.id = subjects[position]?.id!!
            if (sub != null) {
                viewModel.updateSubject(sub)
            }
        }
        holder.delete.setOnClickListener { view ->
            val popup = PopupMenu(context, view)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.actions, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        val sub = subjects[position]?.subjectName?.let { it1 -> SubjectDetails(it1, attended, missed + 1) }
                        sub?.id = subjects[position]?.id!!
                        if (sub != null) {
                            viewModel.delete(sub)
                            notifyDataSetChanged()
                        }
                    }
                }
                true
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var delete: ImageButton
        var decrease: ImageView
        var increase: ImageView
        var ratio: TextView
        var heading: TextView
        var tv_bunk: TextView
        var circleProgress: ArcProgress

        init {
            increase = itemView.findViewById(R.id.increase)
            decrease = itemView.findViewById(R.id.decrease)
            delete = itemView.findViewById(R.id.pop)
            ratio = itemView.findViewById(R.id.qtyTextview)
            heading = itemView.findViewById(R.id.subjectHeading)
            tv_bunk = itemView.findViewById(R.id.tv_bunk)
            circleProgress = itemView.findViewById(R.id.arc_progress)
        }
    }
}