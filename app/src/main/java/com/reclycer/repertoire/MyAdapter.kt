package com.reclycer.repertoire

/**
 * Created by kyouse on 14/11/16.
 */

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import java.io.File
import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import com.reclycer.repertoire.data.Contact
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_cell.*

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var contact: List<Contact>? = ArrayList()
    private var databaseManager: DatabaseManager? = null

    fun refreshContact(context: Context) {
        databaseManager = DatabaseManager(context)
        contact = databaseManager!!.readContact()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (contact != null)
            contact!!.size
        else
            0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_cell, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val fouet = contact!![position]
        holder.display(fouet)
    }

    inner class MyViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {


        private var currentContact: Contact? = null

        init {

            itemView.setOnClickListener {
                val detail_intent = Intent(itemView.context, DetailContactActivity::class.java)
                detail_intent.putExtra("ContactID", currentContact!!.idContact)
                itemView.context.startActivity(detail_intent)
            }
        }

        fun display(personne: Contact) {
            currentContact = personne
            name!!.text = currentContact!!.nom + ' ' + currentContact!!.prenom
            description!!.text = currentContact!!.numero
            if (currentContact!!.photoPath != null) {
                val file = File(currentContact!!.photoPath!!)
                if (file.exists())
                    imageView!!.setImageURI(Uri.fromFile(File(currentContact!!.photoPath!!)))
            }
        }
    }
}