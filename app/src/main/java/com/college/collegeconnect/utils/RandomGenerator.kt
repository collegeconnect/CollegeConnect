package com.college.collegeconnect.utils

import java.security.SecureRandom

class RandomGenerator {

    val s = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    private val rnd= SecureRandom()

    fun randomString(len: Int):String{
        val sb = StringBuilder(len)
        for (i in 0 until len)
            sb.append(s[rnd.nextInt(s.length)])
        return sb.toString()
    }
}