package com.college.collegeconnect.settingsActivity.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.college.collegeconnect.datamodels.Constants
import com.college.collegeconnect.datamodels.Upload
import com.college.collegeconnect.settingsActivity.repository.FetchUploads
import com.college.collegeconnect.utils.FirebaseUtil

class MyFilesViewModel:ViewModel() {

    private val reference = FirebaseUtil.getDatabase().getReference(Constants.DATABASE_PATH_UPLOADS)
    private val fetchUploads = FetchUploads(reference)

    fun getUploads():LiveData<ArrayList<Upload>>{
        return fetchUploads
    }
}