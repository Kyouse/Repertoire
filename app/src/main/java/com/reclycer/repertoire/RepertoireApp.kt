package com.reclycer.repertoire

import android.app.Application
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager

class RepertoireApp : Application(){
    lateinit var databaseManager : DatabaseManager
        private set
    lateinit var dataManager : DataManager
        private set

    override fun onCreate() {
        super.onCreate()

        dataManager = DataManager(this)
        databaseManager = DatabaseManager(this)
    }
}