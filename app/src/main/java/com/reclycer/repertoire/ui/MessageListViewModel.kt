package com.reclycer.repertoire.ui

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager
import com.reclycer.repertoire.data.Message
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class MessageListViewModel: ViewModel(){

    fun initDataManager(applicationContext: Context, toIdContact: String){
        dataManager = DataManager(applicationContext)
        databaseManager = DatabaseManager(applicationContext)
        this.toIdContact = toIdContact
    }

    private lateinit var dataManager: DataManager
    private lateinit var databaseManager: DatabaseManager
    private lateinit var toIdContact: String

    val fromIdContact: String
        get()= databaseManager.currentUser()?.sync_id.toString()
    val messageList: Collection<ListMessageAdapter.MessageWrapper>
        get()=getUpdatedContent()


    fun sendMessage(messageBody: String) : Completable {
        val messageToSend = Message()
        messageToSend.to_id = toIdContact
        messageToSend.from_id = fromIdContact
        messageToSend.date = Calendar.getInstance().toString()
        messageToSend.body = messageBody

       return dataManager.createMessage(messageToSend)
                .observeOn(AndroidSchedulers.mainThread())
    }


    fun refreshFromNetworkCompletable() : Completable {
        return dataManager.refreshMessage()
                .observeOn(AndroidSchedulers.mainThread()).toCompletable()

    }

    private fun getUpdatedContent() : List<ListMessageAdapter.MessageWrapper>{
        val contactList = databaseManager.readContactList()
        val messageList = databaseManager.readMessageList()
                .filter {
                    (it.to_id == toIdContact && it.from_id == fromIdContact) ||
                            (it.to_id == fromIdContact && it.from_id == toIdContact)
                }
                .map {
                    message ->
                    val from = contactList?.firstOrNull{ it.sync_id == message.from_id}
                    val to = contactList?.firstOrNull{ it.sync_id == message.to_id}
                    ListMessageAdapter.MessageWrapper(message, from, to, fromIdContact)}
        return messageList
    }
}