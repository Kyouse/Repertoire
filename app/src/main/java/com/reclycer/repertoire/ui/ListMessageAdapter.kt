package com.reclycer.repertoire.ui

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.reclycer.repertoire.R
import com.reclycer.repertoire.data.Message
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.contact_cell.*
import kotlinx.android.synthetic.main.message_cell.*
import java.io.File
import java.util.ArrayList

class ListMessageAdapter: RecyclerView.Adapter<ListMessageAdapter.MyViewHolder>() {

    val messageList: MutableList<Message> = ArrayList()


    override fun onBindViewHolder(holder: ListMessageAdapter.MyViewHolder, position: Int) {
        val message = messageList!![position]
        holder.display(message)
    }


    override fun getItemCount(): Int {
        return if (messageList != null)
            messageList!!.size
        else
            0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.message_cell, parent, false)
        return MyViewHolder(view)
    }


    inner class MyViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {


/*        private var currentMessage: Message? = null

        init {

            itemView.setOnClickListener {

            }
        } */

        fun display(message: Message) {
            message_from!!.text = "From: " + message.from_id
            message_body!!.text = message.body
        }
    }
}
