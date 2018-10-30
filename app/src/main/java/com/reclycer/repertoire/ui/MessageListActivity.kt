package com.reclycer.repertoire.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.reclycer.repertoire.R
import kotlinx.android.synthetic.main.activity_list_message.*


class MessageListActivity : AppCompatActivity() {

    private val messageAdapter = ListMessageAdapter()


    private lateinit var messageListViewModel: MessageListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_message)
        messageListViewModel = ViewModelProviders.of(this).get(MessageListViewModel::class.java)
        val toIdContact = intent.getStringExtra("ContactID")
        messageListViewModel.initDataManager(applicationContext, toIdContact)

        message_list.layoutManager = LinearLayoutManager(this)
        message_list.adapter = messageAdapter

        refreshFromNetwork()
        displayUpdatedContent()

        button_send!!.setOnClickListener {_->
            onButtonSend()
        }
    }

    private fun onButtonSend() {

        messageListViewModel.sendMessage(edit_message.text.toString()).subscribe({
            edit_message.text = null
            refreshFromNetwork()
        }, {
            Toast.makeText(this@MessageListActivity, "Failed to create message", Toast.LENGTH_SHORT).show()
        })


    }

    private fun refreshFromNetwork() {
        messageListViewModel.refreshFromNetworkCompletable().subscribe({
            displayUpdatedContent()
        }, {
            Toast.makeText(this@MessageListActivity, "Failed to refreshFromNetwork messages", Toast.LENGTH_SHORT).show()
        })
    }

    private fun displayUpdatedContent() {
        messageAdapter.messageList.clear()
        val messageList = messageListViewModel.messageList
        messageAdapter.messageList.addAll(messageList)
        messageAdapter.notifyDataSetChanged()
    }
}