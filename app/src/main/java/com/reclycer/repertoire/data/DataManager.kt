package com.reclycer.repertoire.data

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.reclycer.repertoire.data.fcm.MyRepertoireInstanceIDService
import com.reclycer.repertoire.data.fcm.MyRepertoireMessagingService
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by kyouse on 06/02/18.
 */
class DataManager(val context: Context) {

    val databaseManager = DatabaseManager(context, DatabaseManager.SYNC_DATABASE)

    val retrofit = Retrofit.Builder()
            .baseUrl("http://zombilical.org:1434/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val contactService = retrofit.create(ContactService::class.java)

    fun refresh(): Single<List<ContactService.ApiContact>> {
        return contactService.getContactList()
                .subscribeOn(Schedulers.io()) // Executer sur le thread io
                .doOnSuccess {
                    it.filter { !it.first_name.isNullOrEmpty() && !it.last_name.isNullOrEmpty() }
                            .forEach {
                                val apiContact = it
                                val dbContact = databaseManager.getContact(apiContact._id!!)
                                if (dbContact == null) {
                                    databaseManager.insertContact(apiContact.toDBContact())
                                } else {
                                    dbContact.firstName = apiContact.first_name
                                    dbContact.lastName = apiContact.last_name
                                    dbContact.phoneNumber = apiContact.phone_number
                                    databaseManager.updateContact(dbContact)
                                }
                            }

                   // Log.i("DataManager", "Success to list contact $it")

                }
    }


    fun createContact(contact: Contact): Completable {

        return contactService.createContact(contact.firstName!!, contact.lastName!!, contact.phoneNumber!!, contact?.gcmToken)
                .subscribeOn(Schedulers.io()) // Executer sur le thread io
                .doOnSuccess {
                    val dbContact = it.toDBContact()
                    dbContact.isCurrent = contact.isCurrent
                    if (dbContact.isCurrent ?: true) {
                        dbContact.gcmToken = contact.gcmToken
                    }
                    databaseManager.insertContact(dbContact)
                    Log.i("DataManager", "Success to create contact: $it")

                    databaseManager.deleteContact(contact.idContact)
                    Log.i("DataManager", "Success to delete local contact: ")
                    if (dbContact.isCurrent == true) {
                        context.startService(Intent(context, MyRepertoireInstanceIDService::class.java))
                    }
                }.toCompletable()

    }


    fun updateContact(contact: Contact): Completable {

        return contactService.updateContact(contact.sync_id!!, contact.firstName!!, contact.lastName!!, contact.phoneNumber!!, contact?.gcmToken)
                .subscribeOn(Schedulers.io()) // Executer sur le thread io
                .doOnSuccess {

                    val contactToUpdate = databaseManager.getContact(it._id!!)
                    contactToUpdate!!.firstName = it.first_name
                    contactToUpdate.lastName = it.last_name
                    contactToUpdate.phoneNumber = it.phone_number
                    if (contactToUpdate.isCurrent ?: true) {
                        contactToUpdate.gcmToken = it.gcm_token
                    }
                    if (contactToUpdate.isCurrent == true && contactToUpdate.gcmToken == null) {
                        context.startService(Intent(context, MyRepertoireInstanceIDService::class.java))
                    }
                    databaseManager.updateContact(contactToUpdate)
                }.toCompletable()
    }


    fun deleteApiContact(id: Int) {
        val contactToDelete = databaseManager.getContact(id)
        contactService.deleteContact(contactToDelete!!.sync_id!!)
                .subscribeOn(Schedulers.io()) // Executer sur le thread io
                .observeOn(Schedulers.io())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        databaseManager.deleteContact(id)
                        Log.i("DataManager", "Success to delete contact")

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onError(e: Throwable?) {
                        Log.e("DataManager", "Failed to delete")
                    }


                })


    }

    /*---------------- Message ------------*/


    fun refreshMessage(): Single<List<ContactService.ApiMessage>> {
        return contactService.getMessages()
                .subscribeOn(Schedulers.io()) // Executer sur le thread io
                .doOnSuccess {
                    it.filter { !it.from_id.isNullOrEmpty() && !it.to_id.isNullOrEmpty() }
                            .forEach {
                                val apiMessage = it
                                val dbMessage = databaseManager.getMessage(apiMessage._id!!)
                                if (dbMessage == null) {
                                    databaseManager.insertMessage(apiMessage.toDBMessage())
                                } else {
//                                    dbContact.firstName = apiContact.first_name
//                                    dbContact.lastName = apiContact.last_name
//                                    dbContact.phoneNumber = apiContact.phone_number
                                   // todo databaseManager.updateMessage(dbMessage)
                                }
                            }

                    Log.i("DataManager", "Success to list contact $it")

                }
    }

    fun createMessage(message: Message): Completable {

        return contactService.sendMessage(to_id = message.to_id!!, from_id = message.from_id!!, body = message.body!!)
                .subscribeOn(Schedulers.io()) // Executer sur le thread io
                .doOnSuccess {

                }.toCompletable()

    }

    fun sendAccount(account: GoogleSignInAccount): Completable {

        return contactService.sendAccount("bdkdjq", account.idToken!!)
                .subscribeOn(Schedulers.io()) // Executer sur le thread io
                .doOnSuccess {

                }.toCompletable()
    }
}

private fun ContactService.ApiContact.toDBContact(): Contact {
    val contact = Contact(last_name, first_name, phone_number)
    contact.sync_id = _id
    contact.email = email
    contact.gcmToken = gcm_token
    return contact
}


private fun ContactService.ApiMessage.toDBMessage(): Message {
    val message = Message(from_id = from_id, to_id = to_id, body =  body, date = date)
    message.sync_id = _id
    return message
}