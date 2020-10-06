package com.college.collegeconnect.ui.event.bvest.repository

import com.college.collegeconnect.datamodels.Teams
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class CreateTeamRepository(val eventName:String,val teamName: String, val query: DatabaseReference, val code: String, val list:ArrayList<String>) {

    fun createTeam() {
        val team = Teams(code, teamName, FirebaseAuth.getInstance().currentUser?.displayName)
        query.child("events/$eventName/teams").child(teamName).setValue(team)
        list.add(code)
        query.child("teamCodes").setValue(list)
    }

}