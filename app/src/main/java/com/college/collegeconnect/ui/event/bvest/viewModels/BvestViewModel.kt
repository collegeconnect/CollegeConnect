package com.college.collegeconnect.ui.event.bvest.viewModels

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.college.collegeconnect.datamodels.Constants
import com.college.collegeconnect.datamodels.Events
import com.college.collegeconnect.datamodels.Society
import com.college.collegeconnect.ui.event.bvest.repository.FetchEvent
import com.college.collegeconnect.ui.event.bvest.repository.FetchSocieties
import com.college.collegeconnect.utils.FirebaseUtil
import com.google.firebase.database.*

class BvestViewModel : ViewModel() {

    private val reference2 = FirebaseUtil.getDatabase().getReference(Constants.BVEST_SOCIETY_PATH)
    private val reference = FirebaseUtil.getDatabase().getReference(Constants.BVEST_EVENT_PATH)

    //exposing eventList
    private val eventList: LiveData<ArrayList<Events>> = FetchEvent(reference)

    //exposing societyList
    private val societyList: LiveData<ArrayList<Society>> = FetchSocieties(reference2)


    @NonNull
    fun getEventList(): LiveData<ArrayList<Events>> {
        return eventList
    }

    @NonNull
    fun getSocietyList(): LiveData<ArrayList<Society>> {
        return societyList
    }

}