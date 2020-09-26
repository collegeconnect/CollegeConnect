package com.college.collegeconnect.ui.event.bvest.viewModels

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.college.collegeconnect.datamodels.Constants
import com.college.collegeconnect.datamodels.Events
import com.college.collegeconnect.datamodels.Society
import com.college.collegeconnect.ui.event.bvest.repository.FirebaseQueryLiveData
import com.college.collegeconnect.utils.FirebaseUtil
import com.google.firebase.database.*

class BvestViewModel : ViewModel() {

    var listEvents = MutableLiveData<ArrayList<Events>>()
    var listSociety = MutableLiveData<ArrayList<Society>>()
    val list = arrayListOf<Events>()
    val list2 = arrayListOf<Society>()
    lateinit var reference2: DatabaseReference
    private val reference = FirebaseUtil.getDatabase().getReference(Constants.BVEST_EVENT_PATH)
    //exposing firebaseQueryLiveData
    private val firebaseQueryLiveData: LiveData<ArrayList<Events>> = FirebaseQueryLiveData(reference)

    @NonNull
    fun getList():LiveData<ArrayList<Events>>{
        return firebaseQueryLiveData
    }

    val listener2 = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            list2.clear()
            for (society in snapshot.children) {
                val societies = society.getValue(Society::class.java);
                if (societies != null) {
                    Log.d("BvestActivity", "onDataChange: ${societies.name}")
                    list2.add(societies)
                }
            }
            listSociety.postValue(list2)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("BvestActivity", "onCancelled: "+error.message)
        }

    }

    fun loadSociety() {
        reference2 = FirebaseUtil.getDatabase().getReference(Constants.BVEST_SOCIETY_PATH)
        reference2.addValueEventListener(listener2)
    }

    override fun onCleared() {
        super.onCleared()
        reference2.removeEventListener(listener2)
    }
}