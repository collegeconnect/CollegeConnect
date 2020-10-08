package com.college.collegeconnect.ui.event.bvest.repository

import com.college.collegeconnect.datamodels.TeamMate
import com.college.collegeconnect.datamodels.Teams
import com.google.firebase.database.DatabaseReference
import io.reactivex.rxjava3.core.Completable

class CreateTeamRepository {

    fun createTeam(eventName: String, teamName: String, query: DatabaseReference, code: String, list: ArrayList<String>, phoneNumber: String, teammate1: TeamMate): Completable = Completable.create { emitter ->
        val team = Teams(code, teamName, phoneNumber, teammate1)
        query.child("events/$eventName/teams").child(teamName).setValue(team).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    list.add(code)
                    query.child("teamCodes").setValue(list).addOnSuccessListener {
                        emitter.onComplete()
                    }.addOnFailureListener { exception ->
                        emitter.onError(exception)
                    }

                } else
                    emitter.onError(it.exception!!)
            }
        }

    }

}