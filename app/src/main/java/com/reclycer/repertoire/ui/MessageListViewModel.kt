package com.reclycer.repertoire.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.reclycer.repertoire.RepertoireApp
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager
import com.reclycer.repertoire.data.Message
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class MessageListViewModel: ViewModel() {
    private lateinit var repertoireApp : RepertoireApp

    private val databaseManager : DatabaseManager
        get() = repertoireApp.databaseManager
    private val dataManager : DataManager
        get() = repertoireApp.dataManager

    val currentMessageList = MutableLiveData<List<MessageWrapper>>()

    fun prepare(application: RepertoireApp, toIdContact: String) {
        this.toIdContact = toIdContact
        repertoireApp = application
        updateLiveDataFromDB()
    }

    private lateinit var toIdContact: String

    val fromIdContact: String
        get()= databaseManager.currentUser()?.sync_id.toString()
//    val messageList: Collection<MessageWrapper>
//        get()=getUpdatedContent()


    fun sendMessage(messageBody: String)  {
        val messageToSend = Message()
        messageToSend.to_id = toIdContact
        messageToSend.from_id = fromIdContact
        messageToSend.date = Calendar.getInstance().toString()
        messageToSend.body = messageBody

        dataManager.createMessage(messageToSend)
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ //edit_message.text = null
                    refreshFromNetwor()
    }, {
      //  Toast.makeText(this@MessageListActivity, "Failed to create message", Toast.LENGTH_SHORT).show()
    })
    }


    fun refreshFromNetwor() {
        dataManager.refreshMessage()
                .observeOn(AndroidSchedulers.mainThread()).toCompletable().subscribe({
                    updateLiveDataFromDB()
                    //        }, {
//            Toast.makeText(this@MessageListActivity, "Failed to refreshFromNetwork messages", Toast.LENGTH_SHORT).show()
//        })
                })

    }

    private fun updateLiveDataFromDB() {
        val contactList = databaseManager.readContactList()
        currentMessageList.value = databaseManager.readMessageList()
                .filter {
                    (it.to_id == toIdContact && it.from_id == fromIdContact) ||
                            (it.to_id == fromIdContact && it.from_id == toIdContact)
                }
                .map {
                    message ->
                    val from = contactList?.firstOrNull{ it.sync_id == message.from_id}
                    val to = contactList?.firstOrNull{ it.sync_id == message.to_id}
                    MessageWrapper(message, from, to, fromIdContact)
                }
    }
}