package com.college.collegeconnect.repository

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import com.college.collegeconnect.activities.DownloadNotes
import com.college.collegeconnect.datamodels.Upload
import com.google.firebase.database.*

class DownloadRepository(val query: Query, val bundle: Bundle) : LiveData<ArrayList<Upload>>() {

    constructor(ref: DatabaseReference, bundle: Bundle) : this(query = ref, bundle = bundle)

    val receivedCourse = bundle.getString(DownloadNotes.EXTRA_COURSE)
    val receivedBranch = bundle.getString(DownloadNotes.EXTRA_BRANCH)
    val receivedSemester = bundle.getString(DownloadNotes.EXTRA_SEMESTER)
    val receivedUnit = bundle.getString(DownloadNotes.EXTRA_UNIT)

    val uploadList = arrayListOf<Upload>()

    val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            uploadList.clear()
            for (uploads in snapshot.children) {
                val upload = uploads.getValue(Upload::class.java);
                if (upload != null) {
                    if (upload.getCourse() == receivedCourse) {
                        if (upload.getBranch() == receivedBranch) {
                            if (upload.getSemester() == receivedSemester) {
                                if (upload.getUnit() == receivedUnit) {
                                    uploadList.add(upload)
                                    //Toast.makeText(DownloadNotes.this, upload.getName(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
            //giving value to LiveData<ArrayList<Upload>>
            postValue(uploadList)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("MyFilesViewModel", "onCancelled: called")
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