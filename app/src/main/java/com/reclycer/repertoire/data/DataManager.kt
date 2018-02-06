package com.reclycer.repertoire.data

import android.util.Log
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by kyouse on 06/02/18.
 */
class DataManager{

    val retrofit = Retrofit.Builder()
            .baseUrl("http://zombilical.org:1434/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val contactService = retrofit.create(ContactService::class.java)

    fun refresh(){
        contactService.getContactList()
                .subscribeOn(Schedulers.io()) // Executer sur le thread io
                .observeOn(Schedulers.io()) // Revenir sur le main thread
                .subscribe{ list -> Log.d("LIST","$list")}
    }
}
