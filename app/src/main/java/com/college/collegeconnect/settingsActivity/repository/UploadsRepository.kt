package com.college.collegeconnect.settingsActivity.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.college.collegeconnect.datamodels.Upload
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UploadsRepository(val query: Query) : LiveData<ArrayList<Upload>>() {

    //overloading constructor
    constructor(ref: DatabaseReference) : this(query = ref)

    val list = arrayListOf<Upload>()

    val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            list.clear()
            for (upload in snapshot.children) {
                val uploads = upload.getValue(Upload::class.java);
                if (uploads != null) {
                    if (uploads.getUploaderMail() == FirebaseAuth.getInstance().currentUser?.email)
                        list.add(uploads)
                }
            }
            //giving value to LiveData<ArrayList<Upload>>
            postValue(list)
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