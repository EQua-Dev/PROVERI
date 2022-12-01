package com.androidstrike.proveri.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_checks")
class DBModel(
    @PrimaryKey
//    @ColumnInfo(name = "batch_no")
    var batch_no: String,

//    @ColumnInfo(name = "prod_name")
    var prod_name: String,
//    @ColumnInfo(name = "mfg_name")
    var mfg_name: String,
//    @ColumnInfo(name = "prod_name")
    var prod_date: String,
    var exp_date: String,
    var prod_review: String,
    var is_valid: Boolean
)