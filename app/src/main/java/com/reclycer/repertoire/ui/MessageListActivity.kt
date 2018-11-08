package com.reclycer.repertoire.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.reclycer.repertoire.R
import com.reclycer.repertoire.RepertoireApp
import kotlinx.android.synthetic.main.activity_list_message.*


class MessageListActivity : AppCompatActivity() {

    private val messageAdapter = ListMessageAdapter()
    private lateinit var messageListViewModel: MessageListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_message)
        message_list.layoutManager = LinearLayoutManager(this)
        message_list.adapter = messageAdapter

        val toIdContact = intent.getStringExtra("ContactID")

        messageListViewModel = ViewModelProviders.of(this).get(MessageListViewModel::class.java)
        messageListViewModel.prepare(application as RepertoireApp, toIdContact)
        messageListViewModel.currentMessageList.observe(this, Observer {
            it?.run { displayUpdatedContent(it) }
        })

        button_send!!.setOnClickListener { _ ->
            messageListViewModel.sendMessage(edit_message.text.toString())
        }
    }

    private fun displayUpdatedContent(messageList: List<MessageWrapper>) {
        messageAdapter.messageList.clear()
        messageAdapter.messageList.addAll(messageList)
        messageAdapter.notifyDataSetChanged()
    }
}