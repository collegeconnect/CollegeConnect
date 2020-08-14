package com.college.collegeconnect.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amulyakhare.textdrawable.TextDrawable
import com.college.collegeconnect.Navigation
import com.college.collegeconnect.datamodels.SaveSharedPreference
import com.college.collegeconnect.datamodels.Upload
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso

class HomeViewModel: ViewModel() {

    private var firebaseFirestore: FirebaseFirestore? = null
    var documentReference: DocumentReference? = null
    var registered: ListenerRegistration? = null
    var nameLive : MutableLiveData<String>?= null
    var rollNoLive:MutableLiveData<String>?= null
    var branchLive:MutableLiveData<String>?= null

    fun returnName(): LiveData<String> {
        if (nameLive == null) {
            nameLive = MutableLiveData()
            loadData()
        }
        return nameLive!!
    }

    fun returnRoll(): LiveData<String> {
        if (rollNoLive == null) {
            rollNoLive = MutableLiveData()
            loadData()
        }
        return rollNoLive!!
    }

    fun returnBranch(): LiveData<String> {
        if (branchLive == null) {
            branchLive = MutableLiveData()
            loadData()
        }
        return branchLive!!
    }

    fun loadData(){
        firebaseFirestore = FirebaseFirestore.getInstance()
        documentReference = firebaseFirestore!!.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        firebaseFirestore!!.firestoreSettings = settings

        registered = documentReference!!.addSnapshotListener(MetadataChanges.INCLUDE) { documentSnapshot, error ->
            assert(documentSnapshot != null)
            try {
                val name = documentSnapshot!!.getString("name")
                val rollNo = documentSnapshot!!.getString("rollno")
                val strbranch = documentSnapshot!!.getString("branch")
                nameLive?.postValue(name)
                rollNoLive?.postValue(rollNo)
                branchLive?.postValue(strbranch)
//                SaveSharedPreference.setUser(mcontext, name)
//                nameField.setText(SaveSharedPreference.getUser(mcontext))
//                enrollNo.setText(rollNo)
//                branch.setText(strbranch)
                assert(name != null)
//                val space = name!!.indexOf(" ")
//                val color = Navigation.generatecolor()
//                drawable = TextDrawable.builder().beginConfig()
//                        .width(150)
//                        .height(150)
//                        .bold()
//                        .endConfig()
//                        .buildRound(name!!.substring(0, 1) + name!!.substring(space + 1, space + 2), color)
//                prfileImage.setImageDrawable(drawable)
            } catch (e: Exception) {
                Log.d("Home", "onEvent: " + e.message)
            }
//            if (uri != null) Picasso.get().load(uri).into(prfileImage)
        }
    }
}