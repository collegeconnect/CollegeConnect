package com.college.collegeconnect.ui.event.bvest.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.college.collegeconnect.datamodels.Events
import com.google.firebase.database.*

class EventRepository(val query: Query) : LiveData<ArrayList<Events>>() {

    //overloading constructor
    constructor(ref: DatabaseReference) : this(query = ref)

    val list = arrayListOf<Events>()

    //listener to fetch database from reference
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
            //giving value to LiveData<ArrayList<Event>>
            postValue(list)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("BvestActivity", "onCancelled: " + error.message)
        }
    }
    //attaching listener
    override fun onActive() {
        super.onActive()
        query.addValueEventListener(listener)
    }
    //detaching listener
    override fun onInactive() {
        super.onInactive()
        query.removeEventListener(listener)
    }
}

