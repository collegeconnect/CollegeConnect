package com.college.collegeconnect.ui.event.bvest

interface TaskListener {

    fun complete()
    fun onError(message:String)
}