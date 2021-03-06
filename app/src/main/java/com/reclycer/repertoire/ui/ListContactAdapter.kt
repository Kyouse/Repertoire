package com.reclycer.repertoire.ui

/**
 * Created by kyouse on 14/11/16.
 */

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.reclycer.repertoire.R

import java.io.File
import java.util.ArrayList

import com.reclycer.repertoire.data.Contact
import com.reclycer.repertoire.ui.message.MessageListActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.contact_cell.*

class ListContactAdapter : RecyclerView.Adapter<ListContactAdapter.MyViewHolder>() {

    val contactList: MutableList<Contact> = ArrayList()


    override fun getItemCount(): Int {
        return if (contactList != null)
            contactList!!.size
        else
            0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.contact_cell, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact_list = contactList!![position]
        holder.display(contact_list)
    }

    inner class MyViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {


        private var currentContact: Contact? = null

        init {

            itemView.setOnClickListener {
                val detail_intent = Intent(itemView.context, MessageListActivity::class.java)
                detail_intent.putExtra("ContactID", currentContact!!.sync_id.toString())
                itemView.context.startActivity(detail_intent)
            }
        }

        fun display(personne: Contact) {
            currentContact = personne
            name!!.text = currentContact!!.lastName + ' ' + currentContact!!.firstName
            description!!.text = currentContact!!.phoneNumber
            if (currentContact!!.photoPath != null) {
                val file = File(currentContact!!.photoPath!!)
                if (file.exists())
                    imageView!!.setImageURI(Uri.fromFile(File(currentContact!!.photoPath!!)))
            }
        }
    }
}