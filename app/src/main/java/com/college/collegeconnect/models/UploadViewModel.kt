package com.college.collegeconnect.models


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.college.collegeconnect.datamodels.Constants
import com.college.collegeconnect.datamodels.Upload
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UploadViewModel: ViewModel() {

    var list : MutableLiveData<List<Upload>>? = null
    var reference: DatabaseReference? =null

    fun returnList(): LiveData<List<Upload>> {
        if (list == null) {
            list = MutableLiveData()
            getlist()
        }
        return list!!
    }

    fun getlist(){
        var list2 = ArrayList<Upload>()

        reference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS)
        reference!!.keepSynced(true)
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.d("UploadViewModel", "onCancelled: called")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                list2.clear()
                for (postSnapshot in snapshot.getChildren()) {
                    val upload = postSnapshot.getValue(Upload::class.java)!!
                    if (upload.getUploaderMail() == FirebaseAuth.getInstance().currentUser?.email)
                        list2.add(upload)
                }
                list?.postValue(list2)
            }

        })
    }

    override fun onCleared() {
        super.onCleared()
//            reference?.removeEventListener()
    }
}