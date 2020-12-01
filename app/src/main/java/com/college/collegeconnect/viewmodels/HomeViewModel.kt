package com.college.collegeconnect.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.college.collegeconnect.database.AttendanceDatabase
import com.college.collegeconnect.database.TimeTableDatabase
import com.college.collegeconnect.database.entity.TimetableEntity
import com.college.collegeconnect.datamodels.SaveSharedPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.util.*

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private var firebaseFirestore: FirebaseFirestore? = null
    var documentReference: DocumentReference? = null
    var registered: ListenerRegistration? = null
    var nameLive : MutableLiveData<String>?= null
    var rollNoLive:MutableLiveData<String>?= null
    var branchLive:MutableLiveData<String>?= null
    var classList:MutableLiveData<List<TimetableEntity>>?= null
//    var tot: MutableLiveData<List<Int>>?= null

    fun returnName(): LiveData<String> {
        if (nameLive == null) {
            nameLive = MutableLiveData()
            loadData()
        }
        return nameLive!!
    }

    fun returnNow() : LiveData<List<TimetableEntity>> {
        if (classList == null) {
            classList = MutableLiveData()
            Log.d("TAG", "returnNow: called")
            getHappeningNow()
        }
        Log.d("TAG", "returnNow: ${classList!!.value.toString()}")
        return classList!!
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
//    fun returnTot(): MutableLiveData<List<Int>> {
//        if (tot == null) {
//            tot = MutableLiveData()
//            atten()
//        }
//        return tot!!
//    }

//    fun atten(){
//        viewModelScope.launch { tot?.postValue(listOf( AttendanceDatabase(getApplication()).getAttendanceDao().getAttended(),
//                AttendanceDatabase(getApplication()).getAttendanceDao().getMissed()) )
//          }
//    }

    fun loadData(){
        firebaseFirestore = FirebaseFirestore.getInstance()
        documentReference = firebaseFirestore!!.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        firebaseFirestore!!.firestoreSettings = settings

        registered = documentReference!!.addSnapshotListener(MetadataChanges.INCLUDE) { documentSnapshot, error ->
            try {
                val name = documentSnapshot!!.getString("name")
                val rollNo = documentSnapshot.getString("rollno")
                val strbranch = documentSnapshot.getString("branch")
                val college = documentSnapshot.getString("college")
                SaveSharedPreference.setCollege(getApplication(),college)
                SaveSharedPreference.setUser(getApplication(),name)
                nameLive?.postValue(name)
                rollNoLive?.postValue(rollNo)
                branchLive?.postValue(strbranch)
//                SaveSharedPreference.setUser(mcontext, name)
//                nameField.setText(SaveSharedPreference.getUser(mcontext))
//                enrollNo.setText(rollNo)
//                branch.setText(strbranch)
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

    fun getHappeningNow() : LiveData<out List<TimetableEntity>>{

        val classlist = when(Calendar.getInstance()[Calendar.DAY_OF_WEEK]) {
            1 -> TimeTableDatabase(getApplication()).getSundayDao().getSunClasses()
            2 -> TimeTableDatabase(getApplication()).getMondayDao().getMonClasses()
            3 -> TimeTableDatabase(getApplication()).getTuesdayDao().getTuesClasses()
            4 -> TimeTableDatabase(getApplication()).getWednesdayDao().getWedClasses()
            5 -> TimeTableDatabase(getApplication()).getThursdayDao().getThursClasses()
            6 -> TimeTableDatabase(getApplication()).getFridayDao().getFriClasses()
            7 -> TimeTableDatabase(getApplication()).getSaturdayDao().getSatClasses()
            else -> null
        }
        Log.d("TAG", "getHappeningNow: ${classlist?.value}")
        return classlist!!
    }

    fun getAttended():LiveData<Int>{
        return AttendanceDatabase(getApplication()).getAttendanceDao().getAttended()
    }

    fun getMissed():LiveData<Int>{
        return AttendanceDatabase(getApplication()).getAttendanceDao().getMissed()
    }
}