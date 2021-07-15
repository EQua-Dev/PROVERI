package com.androidstrike.proveri.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.androidstrike.proveri.auth.SignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import java.util.regex.Pattern

object Common {

    //values for shared preferences
    val sharedPrefName= "FirstUse"
    val firstTimeKey = "FirstTime"
    val userNamekey = "userName"
    val departmentKey = "userDepartment"

    var userDepartment: String? = ""

    var userName: String? = SignIn().userName
    var mAuth = FirebaseAuth.getInstance()
    var userId = mAuth.currentUser?.uid
    var userEmail = mAuth.currentUser?.email




    //password regex validator
    val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=.*[0-9])" +  //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +  //any letter
                "(?=.*[@#$%^&+!=])" +    //at least 1 special character
                "(?=\\S+$)" +  //no white spaces
                ".{4,}" +  //at least 4 characters
                "$"
    )

}