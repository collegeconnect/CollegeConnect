package com.college.collegeconnect.datamodels

import java.io.Serializable

data class Teams(val code: String? = null,
                 val teamname: String? = null,
                 val phoneNumber: String? = null,
                 val teammate1: TeamMate? = null,
                 val teammate2: TeamMate? = null,
                 val teammate3: TeamMate? = null,
                 val teammate4: TeamMate? = null) : Serializable