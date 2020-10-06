package com.college.collegeconnect.datamodels

import java.io.Serializable

data class Teams(val code: String? = null,
            val teamname: String? = null,
            val teammate1: String? = null,
            val teammate2: String? = null,
            val teammate3: String? = null,
            val teammate4: String? = null) : Serializable {
}