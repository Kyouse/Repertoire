package com.reclycer.repertoire.data

import android.content.Context
import android.util.Log
import com.reclycer.repertoire.DatabaseManager
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by kyouse on 06/02/18.
 */
class DataManager(context: Context) {

    val databaseManager = DatabaseManager(context, DatabaseManager.SYNC_DATABASE)

    val retrofit = Retrofit.Builder()
            .baseUrl("http://zombilical.org:1434/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val contactService = retrofit.create(ContactService::class.java)

    fun refresh(){
        contactService.getContactList()
                .subscribeOn(Schedulers.io()) // Executer sur le thread io
                .observeOn(Schedulers.io()) // FIXME Revenir sur le main thread
                //      .subscribe{ list -> Log.d("LIST","$list")}
                .subscribe(object:SingleObserver<List<ContactService.ApiContact>>{
                    override fun onSuccess(contactList: List<ContactService.ApiContact>) {
                        contactList
                                .filter { !it.first_name.isNullOrEmpty() && !it.last_name.isNullOrEmpty()  }
                                .forEach {
                                    val apiContact = it
                                    val dbContact = databaseManager.getContact(apiContact._id!!)
                                    if(dbContact == null) {
                                        databaseManager.insertContact(apiContact.toDBContact())
                                    }
                                    else{
                                        dbContact.prenom = apiContact.first_name
                                        dbContact.nom = apiContact.last_name
                                        dbContact.numero = apiContact.phone_number
                                        databaseManager.updateContact(dbContact)
                                    }
                                }

                        Log.i("DataManager", "Success to list contact $contactList")
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onError(e: Throwable?) {
                        Log.i("DataManager", "Failed to subscribe")

                    }
                })
    }

    fun createContact(contact: Contact) {

        contactService.createContact(contact.prenom!!, contact.nom!!, contact.numero!!)
                .subscribeOn(Schedulers.io()) // Executer sur le thread io
                .observeOn(Schedulers.io()) // FIXME Revenir sur le main thread
                //      .subscribe{ list -> Log.d("LIST","$list")}
                .subscribe(object:SingleObserver<ContactService.ApiContact>{
                    override fun onSuccess(apiContact: ContactService.ApiContact) {
                        databaseManager.insertContact(apiContact.toDBContact())
                        Log.i("DataManager", "Success to create contact: $apiContact")
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onError(e: Throwable?) {
                        Log.i("DataManager", "Failed to create")

                    }
                })
    }

    fun updateContact(contact: Contact) {

        contactService.updateContact(contact.sync_id!!, contact.prenom!!, contact.nom!!, contact.numero!!)
                .subscribeOn(Schedulers.io()) // Executer sur le thread io
                .observeOn(Schedulers.io())
                .subscribe(object:SingleObserver<ContactService.ApiContact> {
                    override fun onSuccess(apiContact: ContactService.ApiContact) {

                        val contactToUpdate = databaseManager.getContact(apiContact._id!!)
                        contactToUpdate!!.prenom = apiContact.first_name
                        contactToUpdate!!.nom = apiContact.last_name
                        contactToUpdate!!.numero = apiContact.phone_number
                        databaseManager.updateContact(contactToUpdate)
                    }

                    override fun onSubscribe(d: Disposable?) {}

                    override fun onError(e: Throwable?) {
                        Log.e("DataManager", "Failed to update")
                    }
                })
    }
}

private fun ContactService.ApiContact.toDBContact(): Contact {
    val contact= Contact(last_name, first_name, phone_number)
    contact.sync_id = _id

    return contact
}
