package com.college.collegeconnect.viewmodels

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.college.collegeconnect.database.DownloadDatabase
import com.college.collegeconnect.database.entity.DownloadEntity
import com.college.collegeconnect.datamodels.Constants
import com.college.collegeconnect.datamodels.Upload
import com.college.collegeconnect.repository.DownloadRepository
import com.college.collegeconnect.utils.FirebaseUtil
import kotlinx.coroutines.launch

class DownloadNotesViewModel(application: Application) :AndroidViewModel(application) {

    private val referenceQuery = FirebaseUtil.getDatabase().getReference(Constants.DATABASE_PATH_UPLOADS).orderByChild("download")
    private lateinit var downloadRepository:LiveData<ArrayList<Upload>>

    fun addDownload(downloadEntity: DownloadEntity){
        viewModelScope.launch {
            DownloadDatabase(getApplication()).getDownloadsDao().add(downloadEntity)
        }
    }


    fun downloadNotes(bundle: Bundle):LiveData<ArrayList<Upload>>{
        downloadRepository = DownloadRepository(referenceQuery,bundle)
        return downloadRepository
    }

}