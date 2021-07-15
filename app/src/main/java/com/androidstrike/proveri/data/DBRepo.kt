package com.androidstrike.proveri.data

import androidx.lifecycle.LiveData
import com.androidstrike.proveri.database.ChecksDao
import com.androidstrike.proveri.database.DBModel

class DBRepo(private val checksDao: ChecksDao) {

    val getAllChecks: LiveData<List<DBModel>> = checksDao.getAll()

    suspend fun insertCheck(dbModel: DBModel){
        checksDao.insertCheck(dbModel)
    }
}