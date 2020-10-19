package com.college.collegeconnect.models

import android.app.Application
import androidx.lifecycle.*
import com.college.collegeconnect.datamodels.FirebaseUserInfo
import com.google.firebase.firestore.*

class HomeViewModel(application: Application): AndroidViewModel(application) {

    val nameLive: MutableLiveData<String> = MutableLiveData()
    val rollNoLive: MutableLiveData<String> = MutableLiveData()
    val branchLive: MutableLiveData<String> = MutableLiveData()

//    fun atten(){
//        viewModelScope.launch { tot?.postValue(listOf( AttendanceDatabase(getApplication()).getAttendanceDao().getAttended(),
//                AttendanceDatabase(getApplication()).getAttendanceDao().getMissed()) )
//          }
//    }

    fun loadData(): ListenerRegistration? {
        FirebaseFirestore.getInstance().firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()

        return FirebaseUserInfo.getUserInfo(getApplication()) { user ->
            nameLive.postValue(user.name)
            rollNoLive.postValue(user.rollNo)
            branchLive.postValue(user.branch)
        }
    }
}