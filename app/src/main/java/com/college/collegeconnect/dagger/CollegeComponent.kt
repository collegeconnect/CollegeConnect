package com.college.collegeconnect.dagger

import com.college.collegeconnect.activities.Navigation
import dagger.Component

@Component(modules = [FirebaseModule::class])
interface CollegeComponent {

    fun injectNavigation(navigation: Navigation)
}