package com.college.collegeconnect.settingsActivity.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.college.collegeconnect.database.DownloadDatabase
import com.college.collegeconnect.database.entity.DownloadEntity
import com.college.collegeconnect.datamodels.Constants
import com.college.collegeconnect.datamodels.Upload
import com.college.collegeconnect.settingsActivity.repository.UploadsRepository
import com.college.collegeconnect.utils.FirebaseUtil
import kotlinx.coroutines.launch

class MyFilesViewModel(application: Application) :AndroidViewModel(application) {

    private val reference = FirebaseUtil.getDatabase().getReference(Constants.DATABASE_PATH_UPLOADS)
    private val fetchUploads = UploadsRepository(reference)

    fun getUploads():LiveData<ArrayList<Upload>>{
        return fetchUploads
    }

//    fun getDownloads():LiveData<List<DownloadEntity>>{
//        return DownloadDatabase(getApplication()).getDownloadsDao().getDownloadFiles()
//    }

    fun deleteDownload(id:Int){
        viewModelScope.launch {
            DownloadDatabase(getApplication()).getDownloadsDao().delete(id)
        }
    }
}