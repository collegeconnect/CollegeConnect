package com.college.collegeconnect.ui.event.bvest.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class CheckTeamCodeRepository(val query: DatabaseReference) {

    val list = arrayListOf<String>()
    val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.value != null)
                list.addAll((snapshot.value as ArrayList<String>))
            query.removeEventListener(this)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("CheckTeamRepository", "onCancelled: ${error.message}")
        }

    }
    fun getTeams(): ArrayList<String> {
        query.addListenerForSingleValueEvent(listener)
        return list
    }

}