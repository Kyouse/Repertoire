package com.reclycer.repertoire.ui

import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.reclycer.repertoire.R
import com.reclycer.repertoire.data.Contact
import com.reclycer.repertoire.data.Message
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.message_cell.*
import java.util.ArrayList

class ListMessageAdapter: RecyclerView.Adapter<ListMessageAdapter.MyViewHolder>() {

    val messageList: MutableList<MessageWrapper> = ArrayList()


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


    data class MessageWrapper(val message: Message, val from: Contact?, val to:Contact?, val from_id_contact:String?)

    inner class MyViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {


/*        private var currentMessage: Message? = null

        init {

            itemView.setOnClickListener {

            }
        } */

        fun display(wrapper: MessageWrapper) {



            if (wrapper.from_id_contact == wrapper.from?.sync_id){

                message_from.gravity = Gravity.RIGHT
                message_body.gravity = Gravity.RIGHT
                message_from.setTextColor(Color.BLACK)
                message_body.setTextColor(Color.BLACK)
                card_view.setCardBackgroundColor(Color.LTGRAY)
                linear_message.gravity = Gravity.RIGHT
                itemView.layoutParams
            }
            else{

                message_from.gravity = Gravity.LEFT
                message_body.gravity = Gravity.LEFT
                message_from.setTextColor(Color.WHITE)
                message_body.setTextColor(Color.WHITE)
                card_view.setCardBackgroundColor(Color.rgb(11,80,115))
                linear_message.gravity = Gravity.LEFT
            }

            message_from!!.text = "From: " + (wrapper.from?.firstName ?: "Unknow sender : ${wrapper.message.from_id}")
            message_body!!.text = wrapper.message.body

        }
    }
}