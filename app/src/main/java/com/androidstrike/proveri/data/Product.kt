package com.androidstrike.proveri.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.util.*

//@Entity(tableName = "saved_checks")
data class Product(

//    @PrimaryKey
//    @ColumnInfo(name = "batch_no")
    var batch_no: String? = "",

//    @ColumnInfo(name = "prod_name")
    var prod_name: String? = "",
//    @ColumnInfo(name = "mfg_name")
    var mfg_name: String? = "",
//    @ColumnInfo(name = "prod_name")
    var prod_date: String? = null,
    var exp_date: String? = null

)

