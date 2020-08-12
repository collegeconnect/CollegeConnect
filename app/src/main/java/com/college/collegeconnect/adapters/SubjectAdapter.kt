package com.college.collegeconnect.adapters

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.database.AttendanceDatabase
import com.college.collegeconnect.database.SubjectDetails
import com.college.collegeconnect.datamodels.DatabaseHelper
import com.college.collegeconnect.datamodels.SaveSharedPreference
import com.college.collegeconnect.ui.attendance.AttendanceFragment
import com.github.lzyzsd.circleprogress.ArcProgress
import java.util.*

class SubjectAdapter(private val subjects: ArrayList<String>, private val context: Context) : RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {
    private var dB: DatabaseHelper? = null
    var per = 0
    var criteria = 0f
    var predict = 0f
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subject_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        criteria = SaveSharedPreference.getAttendanceCriteria(context).toFloat()
        val current = subjects[position]
        holder.circleProgress.max = 100
        holder.circleProgress.progress = 25

        //Setting details of cards on loading
        holder.heading.text = current
        val attended = intArrayOf(0)
        val missed = intArrayOf(0)
        dB = DatabaseHelper(context)
        val res = dB!!.getClasses(current)
        if (res.moveToFirst()) {
            attended[0] = res.getString(0).toInt()
            missed[0] = res.getString(1).toInt()
        }
        holder.ratio.text = attended[0].toString() + "/" + (missed[0] + attended[0])
        val percentage = String.format("%.0f", attended[0].toFloat() / (attended[0] + missed[0]) * 100)
        per = if (percentage == "NaN") 0 else percentage.toFloat().toInt()

        // (attended / (attended + miss + 1)*100)
        // (missed+1) / (attended + miss + 1)*100)

        holder.circleProgress.progress = per
        predict = attended[0].toFloat() / (attended[0] + missed[0] + 1) * 100
        val i = arrayOf<String?>(null)
        if (predict < criteria && percentage != "NaN") {
            holder.tv_bunk.text = "You can\'t miss any more lectures"
        } else {
            if (percentage == "NaN") holder.tv_bunk.text = "No classes have happened yet" else {
                i[0] = "1"
                if (attended[0].toFloat() / (attended[0] + missed[0] + 2) * 100 >= criteria) i[0] = "2" else if (attended[0].toFloat() / (attended[0] + missed[0] + 3) * 100 >= criteria) i[0] = "3" else if (attended[0].toFloat() / (attended[0] + missed[0] + 4) * 100 >= criteria) i[0] = "4" else if (i[0] == "4") holder.tv_bunk.text = "You can miss more than 3 lectures" else holder.tv_bunk.text = "You can miss " + i[0] + " lecture(s)"
            }
        }
        holder.circleProgress.progress = per


        //Button functionality
        holder.increase.setOnClickListener {
            attended[0]++
            dB!!.updateData(Integer.toString(position + 1), current, Integer.toString(attended[0]), Integer.toString(missed[0]))
            holder.ratio.text = attended[0].toString() + "/" + (missed[0] + attended[0])
            val percentage = String.format("%.0f", attended[0].toFloat() / (attended[0] + missed[0]) * 100)
            per = percentage.toFloat().toInt()
            predict = attended[0].toFloat() / (attended[0] + missed[0] + 1) * 100
            //                predict = missed[0] + 1;
            Log.d(TAG, "onClick: increase $predict")
            if (predict < criteria && percentage != "NaN") {
                holder.tv_bunk.text = "You can\'t miss any more lectures"
            } else {
                i[0] = "1"
                if (attended[0].toFloat() / (attended[0] + missed[0] + 2) * 100 >= criteria) i[0] = "2"
                if (attended[0].toFloat() / (attended[0] + missed[0] + 3) * 100 >= criteria) i[0] = "3"
                if (attended[0].toFloat() / (attended[0] + missed[0] + 4) * 100 >= criteria) i[0] = "4"
                if (i[0] == "4") holder.tv_bunk.text = "You can miss more than 3 lectures" else holder.tv_bunk.text = "You can miss " + i[0] + " lecture(s)"
            }
            holder.circleProgress.progress = per
        }
        holder.decrease.setOnClickListener {
            missed[0]++
            dB!!.updateData(Integer.toString(position + 1), current, Integer.toString(attended[0]), Integer.toString(missed[0]))
            holder.ratio.text = attended[0].toString() + "/" + (missed[0] + attended[0])
            val percentage = String.format("%.0f", attended[0].toFloat() / (attended[0] + missed[0]) * 100)
            per = percentage.toFloat().toInt()
            predict = attended[0].toFloat() / (attended[0] + missed[0] + 1) * 100
            Log.d(TAG, "onClick: increase $predict")
            if (predict < criteria && percentage != "NaN") {
                holder.tv_bunk.text = "You can\'t miss any more lectures"
            } else {
                i[0] = "1"
                if (attended[0].toFloat() / (attended[0] + missed[0] + 2) * 100 >= criteria) i[0] = "2"
                if (attended[0].toFloat() / (attended[0] + missed[0] + 3) * 100 >= criteria) i[0] = "3"
                if (attended[0].toFloat() / (attended[0] + missed[0] + 4) * 100 >= criteria) i[0] = "4"
                if (i[0] == "4") holder.tv_bunk.text = "You can miss more than 3 lectures" else holder.tv_bunk.text = "You can miss " + i[0] + " lecture(s)"
            }
            holder.circleProgress.progress = per
        }
        holder.delete.setOnClickListener { view ->
            val popup = PopupMenu(context, view)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.actions, popup.menu)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        dB!!.deleteData(subjects[position])
                        subjects.removeAt(position)
                        AttendanceFragment.notifyChange()
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

    companion object {
        private const val TAG = "SubjectAdapter"
    }

}