package com.reclycer.repertoire.data.fcm

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat.invalidateOptionsMenu
import android.util.Log
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class MyRepertoireInstanceIDService: FirebaseInstanceIdService(){

    override fun onCreate() {
        super.onCreate()
        val refreshedToken = FirebaseInstanceId.getInstance().getToken()
        logTag("Token already available: $refreshedToken")
        updateCurrentUserByAddingTokenAndSendItToServer(refreshedToken)
    }

    override fun onTokenRefresh() {
        super.onTokenRefresh()

        val refreshedToken = FirebaseInstanceId.getInstance().getToken()
        logTag("Refreshed Token: $refreshedToken")
        updateCurrentUserByAddingTokenAndSendItToServer(refreshedToken)
    }

    private fun updateCurrentUserByAddingTokenAndSendItToServer(refreshedToken: String?) {

//        databaseupdate
        val databaseManager = DatabaseManager(this)
        val dataManager = DataManager(this)
        val currentUser = databaseManager.currentUser()

        currentUser?.let{
            it.gcmToken = refreshedToken
            databaseManager.updateContact(it)
            dataManager.updateContact(it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CompletableObserver {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable?) {

                        }

                        override fun onError(e: Throwable?) {
                            Toast.makeText(this@MyRepertoireInstanceIDService, "Failed to update contact", Toast.LENGTH_SHORT).show()
                        }

                    })
        }

    }

    fun Any.logTag(msg:String?){
        Log.d(this.javaClass.name,"$msg")
    }
}