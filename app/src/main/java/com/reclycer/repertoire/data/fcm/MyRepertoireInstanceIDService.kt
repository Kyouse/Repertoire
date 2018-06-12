package com.reclycer.repertoire.data.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyRepertoireInstanceIDService: FirebaseInstanceIdService(){

    override fun onCreate() {
        super.onCreate()
        val refreshedToken = FirebaseInstanceId.getInstance().getToken()
        Log.d("Token", "Token already available: $refreshedToken")
    }

    override fun onTokenRefresh() {
        super.onTokenRefresh()

        val refreshedToken = FirebaseInstanceId.getInstance().getToken()
        Log.d("Token", "Refreshed Token: $refreshedToken")
    }
}