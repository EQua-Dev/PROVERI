package com.androidstrike.proveri.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.androidstrike.proveri.database.ChecksDatabase
import com.androidstrike.proveri.database.DBModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChecksViewModel(application: Application): AndroidViewModel(application) {

    val getAllChecks: LiveData<List<DBModel>>
    private val repo: DBRepo

    init {
        val checksDao = ChecksDatabase.invokeDB(application).checksDao() //gets the DAO from the DB class instead of directly
        repo = DBRepo(checksDao) //pass the dao from the db into the repo
        getAllChecks = repo.getAllChecks //assign the val in this VModel to the function from the repo
    }

    fun insertCheck(dbModel: DBModel){
        viewModelScope.launch(Dispatchers.IO) {
            //coroutine
            repo.insertCheck(dbModel)
        }
    }
}