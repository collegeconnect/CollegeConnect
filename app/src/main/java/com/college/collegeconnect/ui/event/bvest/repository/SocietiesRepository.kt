package com.college.collegeconnect.ui.event.bvest.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.college.collegeconnect.datamodels.Society
import com.google.firebase.database.*

class SocietiesRepository(val query: Query) : LiveData<ArrayList<Society>>() {

    constructor(ref: DatabaseReference) : this(query = ref)

    val list = arrayListOf<Society>()

    val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            list.clear()
            for (society in snapshot.children) {
                val societies = society.getValue(Society::class.java);
                if (societies != null) {
                    Log.d("BvestActivity", "onDataChange: ${societies.name}")
                    list.add(societies)
                }
            }
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