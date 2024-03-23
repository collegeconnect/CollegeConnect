package com.college.collegeconnect.timetable

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.TimetableAdapter
import com.college.collegeconnect.database.TimeTableDatabase
import com.college.collegeconnect.database.entity.*
import java.util.ArrayList

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment(private val position: Int, private var newTimeTableViewModel: NewTimeTableViewModel) : Fragment() {

    private lateinit var subjectRecycler: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_placeholder, container, false)
        subjectRecycler = view.findViewById(R.id.ttRecyclerView)
        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subjectRecycler.setHasFixedSize(true)
        subjectRecycler.layoutManager = LinearLayoutManager(context)
        load()
    }

    private fun load() {

        val subject = context?.let {
            when (position) {
                0 -> TimeTableDatabase(it).getMondayDao().getMonClasses()
                1 -> TimeTableDatabase(it).getTuesdayDao().getTuesClasses()
                2 -> TimeTableDatabase(it).getWednesdayDao().getWedClasses()
                3 -> TimeTableDatabase(it).getThursdayDao().getThursClasses()
                4 -> TimeTableDatabase(it).getFridayDao().getFriClasses()
                5 -> TimeTableDatabase(it).getSaturdayDao().getSatClasses()
                6 -> TimeTableDatabase(it).getSundayDao().getSunClasses()
                else -> null
            }
        }
        subject?.observe(requireActivity(), {
            val subjectList = ArrayList<TimetableEntity>()
            subjectList.addAll(it)
            subjectAdapter = context?.let { it1 -> TimetableAdapter(subjectList, it1, newTimeTableViewModel, position) }!!
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