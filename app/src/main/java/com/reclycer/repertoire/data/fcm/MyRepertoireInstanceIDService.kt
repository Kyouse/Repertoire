package com.reclycer.repertoire.data.fcm

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat.invalidateOptionsMenu
import android.util.Log
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.reclycer.repertoire.BuildConfig
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MyRepertoireInstanceIDService: FirebaseInstanceIdService(){

    var hasRefreshedOnce = false

    override fun onCreate() {
        super.onCreate()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        logTag("Token already available: $refreshedToken")
        if(BuildConfig.DEBUG and !hasRefreshedOnce and !refreshedToken.isNullOrEmpty()){
            Completable.fromAction {
                FirebaseInstanceId.getInstance().deleteInstanceId()
                logTag("Delete available Token: $refreshedToken")
            }.subscribeOn(Schedulers.io()).subscribe()
            hasRefreshedOnce = true
        }
        else {
            updateCurrentUserByAddingTokenAndSendItToServer(refreshedToken)
        }
    }

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        logTag("Refreshed Token: $refreshedToken")
        updateCurrentUserByAddingTokenAndSendItToServer(refreshedToken)
    }


    private fun updateCurrentUserByAddingTokenAndSendItToServer(refreshedToken: String?) {

//        databaseupdate
        val databaseManager = DatabaseManager(this)
        val dataManager = DataManager(this)
        val currentUser = databaseManager.currentUser()

        currentUser?.let {
            if (it.sync_id != null) {
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

    }

}
//If we want this function to be available everywhere it needs to be declared outside the class
fun Any.logTag(msg:String?){
    Log.d(this.javaClass.name,"$msg")
}