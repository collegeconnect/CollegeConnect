package com.college.collegeconnect.ui.event.bvest.viewModels

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.college.collegeconnect.datamodels.*
import com.college.collegeconnect.ui.event.bvest.TaskListener
import com.college.collegeconnect.ui.event.bvest.repository.CheckTeamCodeRepository
import com.college.collegeconnect.ui.event.bvest.repository.CreateTeamRepository
import com.college.collegeconnect.ui.event.bvest.repository.EventRepository
import com.college.collegeconnect.ui.event.bvest.repository.SocietiesRepository
import com.college.collegeconnect.utils.FirebaseUtil
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class BvestViewModel : ViewModel() {

    private val firebaseDatabase: FirebaseDatabase = FirebaseUtil.getDatabase()
    private lateinit var databaseReference: DatabaseReference
    private val reference2 = FirebaseUtil.getDatabase().getReference(Constants.BVEST_SOCIETY_PATH)
    private val reference = FirebaseUtil.getDatabase().getReference(Constants.BVEST_EVENT_PATH)
    var teamData: MutableLiveData<Teams>? = null
    var listener: ValueEventListener? = null
    lateinit var eventName:String
    private val disposables = CompositeDisposable()
    var taskListener:TaskListener? =null
    private val createTeamRepository = CreateTeamRepository()

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
        databaseReference.addValueEventListener(listener as ValueEventListener)
    }

    fun createTeam(teamName:String, code: String,list:ArrayList<String>,phoneNumber:String,teammate1:TeamMate) {
        databaseReference = firebaseDatabase.getReference("Bvest")
        val disposable = createTeamRepository.createTeam(eventName,teamName, databaseReference,code,list,phoneNumber,teammate1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    taskListener?.complete()
                },{
                    taskListener?.onError(it.message!!)
                })
        disposables.add(disposable)
    }
    //check if team code already exists
    fun checkTeamCode():ArrayList<String>{
        databaseReference = FirebaseUtil.getDatabase().getReference("Bvest/teamCodes")
        return CheckTeamCodeRepository(databaseReference).getTeams()
    }
    override fun onCleared() {
        listener?.let { databaseReference.removeEventListener(it) }
        disposables.dispose()
    }
}