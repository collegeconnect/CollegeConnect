package com.college.collegeconnect.ui.event.bvest.viewModels

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.college.collegeconnect.datamodels.Constants
import com.college.collegeconnect.datamodels.Events
import com.college.collegeconnect.ui.event.bvest.repository.FirebaseQueryLiveData
import com.college.collegeconnect.utils.FirebaseUtil

class BvestViewModel : ViewModel() {

    private val reference = FirebaseUtil.getDatabase().getReference(Constants.BVEST_EVENT_PATH)
    //exposing firebaseQueryLiveData
    private val firebaseQueryLiveData: LiveData<ArrayList<Events>> = FirebaseQueryLiveData(reference)

    @NonNull
    fun getList():LiveData<ArrayList<Events>>{
        return firebaseQueryLiveData
    }

}