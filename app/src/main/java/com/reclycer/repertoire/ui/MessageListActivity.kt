package com.reclycer.repertoire.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.reclycer.repertoire.R
import com.reclycer.repertoire.data.ContactService
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager
import com.reclycer.repertoire.data.Message
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_list_message.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class MessageListActivity : AppCompatActivity() {

    private val messageAdapter = ListMessageAdapter()
    lateinit var databasemanager: DatabaseManager
    lateinit var dataManager: DataManager
    lateinit var to_id_contact: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_message)

        to_id_contact = intent.getStringExtra("ContactID")

        message_list.layoutManager = LinearLayoutManager(this)
        message_list.adapter = messageAdapter
        databasemanager = DatabaseManager(this)
        dataManager = DataManager(this)
        refresh(dataManager)

        displayUpdatedContent()

        button_send!!.setOnClickListener { view ->
            onButtonSend(view)
        }
    }

    private fun onButtonSend(view: View?) {
        val current_message = Message()
        current_message.to_id = to_id_contact
        current_message.from_id = databasemanager.currentUser()?.idContact.toString()
        current_message.date = Calendar.getInstance().toString()
        current_message.body = edit_message.text.toString()

        dataManager.createMessage(current_message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                       displayUpdatedContent()
                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onError(e: Throwable?) {
                        Toast.makeText(this@MessageListActivity, "Failed to create message", Toast.LENGTH_SHORT).show()
                    }

                })
    }

    private fun refresh(datamanager: DataManager) {
        datamanager.refreshMessage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<ContactService.ApiMessage>> {
                    override fun onSuccess(contactList: List<ContactService.ApiMessage>) {
                        displayUpdatedContent()
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onError(e: Throwable?) {
                        Log.i("DataManager", "Failed to subscribe")
                    }
                })
    }

    private fun displayUpdatedContent() {
        messageAdapter.messageList.clear()
        val messageList = databasemanager.readMessageList().toList()
        messageAdapter.messageList.addAll(messageList)
        messageAdapter.notifyDataSetChanged()
    }
}