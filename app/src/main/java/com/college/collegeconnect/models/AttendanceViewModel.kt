package com.college.collegeconnect.models

import android.app.Application
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.college.collegeconnect.database.AttendanceDatabase
import com.college.collegeconnect.database.SubjectDetails
import com.college.collegeconnect.datamodels.Upload
import com.college.collegeconnect.ui.attendance.AttendanceFragment
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.launch

class AttendanceViewModel(application: Application) : AndroidViewModel(application) {

//    fun returnList(): LiveData<List<String>> {
//        if (list == null) {
//            list = MutableLiveData()
//            loadData()
//        }
//        return list!!
//    }

    fun addSubject(name: String) {
        viewModelScope.launch {
            val subject = SubjectDetails(name, 0, 0)
            AttendanceDatabase(getApplication()).getAttendanceDao().add(subject)
            Toast.makeText(getApplication(), "Subject added successfully", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateSubject(subjectDetails: SubjectDetails) {

        viewModelScope.launch {
            AttendanceDatabase(getApplication()).getAttendanceDao().update(subjectDetails)
        }
    }
    fun delete( id: Int){
        viewModelScope.launch{
            AttendanceDatabase(getApplication()).getAttendanceDao().delete(id)
        }
    }

//    fun getSubjects() : List<String> {
//        var list = ArrayList<String>()
//        viewModelScope.launch {
//            list = AttendanceDatabase(getApplication()).getAttendanceDao().getSubjects() as ArrayList<String>
//        }
//        return list
//    }

}