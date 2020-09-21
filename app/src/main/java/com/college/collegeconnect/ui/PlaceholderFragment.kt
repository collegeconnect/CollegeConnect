package com.college.collegeconnect.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.SubjectAdapter
import com.college.collegeconnect.adapters.TimetableAdapter
import com.college.collegeconnect.database.AttendanceDatabase
import com.college.collegeconnect.database.SubjectDetails
import com.college.collegeconnect.database.TimeTableDatabse
import com.college.collegeconnect.ui.attendance.AttendanceFragment
import java.util.ArrayList

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment(private val position: Int) : Fragment() {

    private lateinit var subjectRecycler: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_placeholder, container, false)
        subjectRecycler = view.findViewById(R.id.ttRecyclerView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        subjectRecycler.setHasFixedSize(true)
        subjectRecycler.layoutManager = LinearLayoutManager(context)
        load()
    }

    private fun load() {

        val subject = context?.let {
            TimeTableDatabse(it).getMondayDao().getSubjects()
        }
        subject?.observe(requireActivity(), Observer {
            val subjectList = ArrayList<String>()
            subjectList.addAll(it)
            PlaceholderFragment.subjectAdapter = context?.let { it1 -> TimetableAdapter(subjectList, it1) }!!
            subjectRecycler.adapter = PlaceholderFragment.subjectAdapter
        })
    }

    companion object {
        lateinit var subjectAdapter: TimetableAdapter
        fun notifyChange() {
            subjectAdapter.notifyDataSetChanged()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}