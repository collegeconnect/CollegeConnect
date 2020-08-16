package com.college.collegeconnect.ui.attendance

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.SubjectAdapter
import com.college.collegeconnect.database.AttendanceDatabase
import com.college.collegeconnect.database.SubjectDetails
import com.college.collegeconnect.datamodels.DatabaseHelper
import com.college.collegeconnect.models.AttendanceViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_attendance.*

class AttendanceFragment : Fragment(){
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var mydb: DatabaseHelper
    lateinit var subject: TextInputLayout
    lateinit var addSubject: Button
    private lateinit var subjectRecycler: RecyclerView
    lateinit var tv: TextView
    lateinit var mCtx: Context
    lateinit var subjectList: ArrayList<SubjectDetails?>
    private lateinit var viewModel: AttendanceViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_attendance, container, false)
        subjectRecycler = view.findViewById(R.id.subjectRecyclerView)
        subject = view.findViewById(R.id.subjectNamemas)
        addSubject = view.findViewById(R.id.addSubject)
        subject.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                subject.error = null
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(AttendanceViewModel::class.java)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv = requireActivity().findViewById(R.id.navTitle)
        tv.text = "ATTENDANCE"
        if (activity != null) bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        mydb = DatabaseHelper(context)
        subjectList = ArrayList()
        subjectRecycler.setHasFixedSize(true)
        subjectRecycler.layoutManager = LinearLayoutManager(context)
        load()
        addSubject.setOnClickListener { addSubject() }
    }

//    private fun loadData() {
//        launch {
//            context?.let {
//                val subject = AttendanceDatabase(it).getAttendanceDao().getAttendance()
//                for(sub in subject){
//                    subjectList.add(sub.subjectName)
//                }
//                subjectAdapter.notifyDataSetChanged()
//            }
//        }
////        val res = mydb!!.viewAllData()
////        while (res.moveToNext()) {
////            subjectList!!.add(res.getString(1))
////            subjectAdapter!!.notifyDataSetChanged()
////        }
//    }

    private fun load() {
        val subjectList = ArrayList<SubjectDetails?>()
        val subject = context?.let {
            AttendanceDatabase(it).getAttendanceDao().getAttendance()}
            subject?.observe(viewLifecycleOwner, Observer {
                subjectList.clear()
                    for (sub in it) {
                        subjectList.add(sub)
                    }
                subjectAdapter = SubjectAdapter(subjectList, mCtx, viewModel)
                subjectRecycler.adapter = subjectAdapter
                notifyChange()
                subjectAdapter.notifyDataSetChanged()
            })
    }
    private fun addSubject() {
        if (subject.editText!!.text.toString().isEmpty() || subject.editText!!.text.toString() == "") subject.error = "Enter a Subject" else {
            viewModel.addSubject(subject.editText!!.text.toString())
            try {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity?.currentFocus!!.windowToken, 0)
            } catch (e: Exception) { }
            subjectAdapter.notifyDataSetChanged()
            subject.editText!!.setText("")
            subject.clearFocus()
        }


//    private fun addSubject() {
//        if (subject.editText!!.text.toString().isEmpty() || subject.editText!!.text.toString() == "") subject.error = "Enter a Subject" else {
//            launch {
//                val subject = SubjectDetails(subject.editText!!.text.toString(),0,0)
//                context?.let {
//                    AttendanceDatabase(it).getAttendanceDao().add(subject)
//                    Toast.makeText(it, "Subject added successfully", Toast.LENGTH_SHORT).show()
//                    try {
//                        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                        imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
//                    } catch (e: Exception) {
//
//                    }
//                }
//            }
//            subjectList.add(subject.editText!!.text.toString())
//            subjectAdapter.notifyDataSetChanged()
//            subject.editText!!.setText("")
//            subject.clearFocus()
//        }
//        if (subject!!.editText!!.text.toString().isEmpty() || subject!!.editText!!.text.toString() == "") subject!!.error = "Enter a Subject" else {
//            val res = mydb!!.insetData(subject!!.editText!!.text.toString(), "0", "0")
//            if (res == true) {
//                Toast.makeText(context, "Subject added successfully", Toast.LENGTH_SHORT).show()
//            } else Toast.makeText(context, "Data not added", Toast.LENGTH_SHORT).show()
//            try {
//                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
//            } catch (e: Exception) {
//                //
//            }
//            subjectList!!.add(subject.editText!!.text.toString())
//            subjectAdapter!!.notifyDataSetChanged()
//            subject.editText!!.setText("")
//            subject.clearFocus()
//        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCtx = context
    }

    override fun onStart() {
        super.onStart()
        bottomNavigationView.menu.findItem(R.id.nav_attendance).isChecked = true
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.menu.findItem(R.id.nav_attendance).isChecked = true
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        job.cancel()
//    }

    companion object {
        lateinit var subjectAdapter: SubjectAdapter
        fun notifyChange() {
            subjectAdapter.notifyDataSetChanged()
        }
    }
}