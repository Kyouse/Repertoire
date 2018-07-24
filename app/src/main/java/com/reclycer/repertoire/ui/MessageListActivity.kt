package com.reclycer.repertoire.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.reclycer.repertoire.R
import com.reclycer.repertoire.data.ContactService
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_list_message.*



class MessageListActivity : AppCompatActivity() {

    private val messageAdapter = ListMessageAdapter()
    lateinit var databasemanager: DatabaseManager
    lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_message)

        message_list.layoutManager = LinearLayoutManager(this)
        message_list.adapter = messageAdapter
        databasemanager = DatabaseManager(this)
        dataManager = DataManager(this)
        refresh(dataManager)

        displayUpdatedContent()
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