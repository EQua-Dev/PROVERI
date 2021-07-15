package com.androidstrike.proveri.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androidstrike.proveri.data.Product

@Dao
interface ChecksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCheck(dbModel: DBModel)

    @Delete
    fun delete(dbModel: DBModel)

    @Query("select * from saved_checks")
    fun getAll(): LiveData<List<DBModel>>
}