package com.college.collegeconnect.ui.event.bvest.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.college.collegeconnect.datamodels.Constants
import com.college.collegeconnect.datamodels.Events
import com.college.collegeconnect.utils.FirebaseUtil
import com.google.firebase.database.*

class BvestViewModel : ViewModel() {

    var listEvents = MutableLiveData<ArrayList<Events>>()
    val list = arrayListOf<Events>()
    lateinit var reference: DatabaseReference

    val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            list.clear()
            for (event in snapshot.children) {
                val events = event.getValue(Events::class.java);
                if (events != null) {
                    Log.d("BvestActivity", "onDataChange: ${events.eventName}")
                    list.add(events)
                }
            }
            listEvents.postValue(list)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("BvestActivity", "onCancelled: "+error.message)
        }

    }

    fun loadData() {
        reference = FirebaseUtil.getDatabase().getReference(Constants.BVEST_EVENT_PATH)
        reference.addValueEventListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        reference.removeEventListener(listener)
    }
}