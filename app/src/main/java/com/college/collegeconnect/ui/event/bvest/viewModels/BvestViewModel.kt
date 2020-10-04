package com.college.collegeconnect.ui.event.bvest.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.college.collegeconnect.datamodels.Constants
import com.college.collegeconnect.datamodels.Events
import com.college.collegeconnect.datamodels.Society
import com.college.collegeconnect.datamodels.Teams
import com.college.collegeconnect.ui.event.bvest.repository.EventRepository
import com.college.collegeconnect.ui.event.bvest.repository.SocietiesRepository
import com.college.collegeconnect.utils.FirebaseUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class BvestViewModel : ViewModel() {

    val firebaseDatabase = FirebaseUtil.getDatabase()
    var databaseReference: DatabaseReference? = null
    private val reference2 = FirebaseUtil.getDatabase().getReference(Constants.BVEST_SOCIETY_PATH)
    private val reference = FirebaseUtil.getDatabase().getReference(Constants.BVEST_EVENT_PATH)
    var teamData: MutableLiveData<Teams>? = null
    var listener: ValueEventListener? = null

    //exposing eventList
    private val eventList: LiveData<ArrayList<Events>> = EventRepository(reference)

    //exposing societyList
    private val societyList: LiveData<ArrayList<Society>> = SocietiesRepository(reference2)


    @NonNull
    fun getEventList(): LiveData<ArrayList<Events>> {
        return eventList
    }

    @NonNull
    fun getSocietyList(): LiveData<ArrayList<Society>> {
        return societyList
    }

    fun returnTeam(eventName: String, code: String): LiveData<Teams> {
        if (teamData == null) {
            teamData = MutableLiveData()
            loadTeamDetails(eventName, code)
        }
        return teamData!!
    }

    fun loadTeamDetails(eventName: String, code: String) {

        databaseReference = firebaseDatabase.getReference("Bvest/events/$eventName/teams")
//        Log.d("TAG", "loadTeamDetails: ${databaseReference!!.path.toString()}")
        //listener to fetch database from reference
        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (team in snapshot.children) {
                    val teams = team.getValue(Teams::class.java)
                    if (teams != null && teams.code == code) {
                        teamData!!.postValue(teams)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("BvestActivity", "onCancelled: " + error.message)
            }
        }
        databaseReference!!.addValueEventListener(listener as ValueEventListener)
    }

    override fun onCleared() {
        listener?.let { databaseReference?.removeEventListener(it) }
    }
}