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
import com.college.collegeconnect.adapters.TimetableAdapter
import com.college.collegeconnect.database.TimeTableDatabse
import com.college.collegeconnect.database.entity.*
import kotlinx.android.synthetic.main.activity_new_time_table.*
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
            when (position) {
                0 -> TimeTableDatabse(it).getMondayDao().getMonClasses()
                1 -> TimeTableDatabse(it).getTuesdayDao().getTuesClasses()
                2 -> TimeTableDatabse(it).getWednesdayDao().getWedClasses()
                3 -> TimeTableDatabse(it).getThursdayDao().getThursClasses()
                4 -> TimeTableDatabse(it).getFridayDao().getFriClasses()
                5 -> TimeTableDatabse(it).getSaturdayDao().getSatClasses()
                6 -> TimeTableDatabse(it).getSundayDao().getSunClasses()
                else -> null
            }
        }
        subject?.observe(requireActivity(), {
            val subjectList = ArrayList<TimetableEntity>()
            subjectList.addAll(it)
            subjectAdapter = context?.let { it1 -> TimetableAdapter(subjectList, it1) }!!
            subjectRecycler.adapter = subjectAdapter
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