package com.college.collegeconnect.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.college.collegeconnect.database.AttendanceDatabase
import com.college.collegeconnect.database.entity.SubjectDetails
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

    fun getAttended():LiveData<Int>{
        return AttendanceDatabase(getApplication()).getAttendanceDao().getAttended()
    }

    fun getMissed():LiveData<Int>{
        return AttendanceDatabase(getApplication()).getAttendanceDao().getMissed()
    }
//    fun getSubjects() : List<String> {
//        var list = ArrayList<String>()
//        viewModelScope.launch {
//            list = AttendanceDatabase(getApplication()).getAttendanceDao().getSubjects() as ArrayList<String>
//        }
//        return list
//    }

}