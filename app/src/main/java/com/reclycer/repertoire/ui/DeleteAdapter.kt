package com.reclycer.repertoire.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.reclycer.repertoire.R

import java.io.File
import java.util.ArrayList

import com.reclycer.repertoire.data.Contact
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager

/**
 * Created by kyouse on 03/12/17.
 */

class DeleteAdapter : RecyclerView.Adapter<DeleteAdapter.MyViewHolder>() {

    val contactList: MutableList<Contact> = ArrayList()
    val listIdsToRemove = ArrayList<Int>()


    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.todelete, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.display(contactList[position])
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView
        private val description: TextView
        private var currentContact: Contact? = null
        private val checkBox: CheckBox
        private val imageView: ImageView

        init {

            name = itemView.findViewById(R.id.name_del) as TextView
            description = itemView.findViewById(R.id.description_del) as TextView
            checkBox = itemView.findViewById(R.id.chkSelected) as CheckBox
            imageView = itemView.findViewById(R.id.imageView_del) as ImageView

            itemView.setOnClickListener {
                if (checkBox.isChecked) {
                    checkBox.isChecked = false
                    listIdsToRemove.remove(Integer.valueOf(currentContact!!.idContact))
                } else {
                    checkBox.isChecked = true
                    listIdsToRemove.add(currentContact!!.idContact)
                }
            }
        }

        fun display(personne: Contact) {
            currentContact = personne
            name.text = currentContact!!.nom + ' ' + currentContact!!.prenom
            description.text = currentContact!!.numero
            if (currentContact!!.photoPath != null) {
                val file = File(currentContact!!.photoPath!!)
                if (file.exists())
                // imageView.setImageURI(Uri.fromFile(File(currentContact!!.photoPath!!)))
                    Glide.with(name.context)
                            .load(currentContact!!.photoPath!!)
                            .centerCrop()
                            .into(imageView);
            }
        }
    }
}
