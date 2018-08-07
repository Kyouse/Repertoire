package com.reclycer.repertoire.data.fcm

import android.os.Handler
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.os.Looper



class MyRepertoireMessagingService: FirebaseMessagingService(){
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        remoteMessage?.let{
            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
            logTag( "From: " + remoteMessage.from)
0


           // Check if message contains a data payload.
            val data = remoteMessage.data
            if (data.isNotEmpty()) {
                logTag( "Message data payload: " + remoteMessage.data)
                //We need to show the toast on the UI thread...
                //https://stackoverflow.com/questions/41729152/display-messagebody-of-firebase-notification-as-toast
                val handler = Handler(Looper.getMainLooper())
                handler.post { Toast.makeText(this,  "Message received : $data", Toast.LENGTH_SHORT).show() }


            }
            // Check if message contains a notification payload.
            if (remoteMessage.notification != null) {
                logTag( "Message Notification Body: " + remoteMessage.notification?.body)
            }
        }

    }
}