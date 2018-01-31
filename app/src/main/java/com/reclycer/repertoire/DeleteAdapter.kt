package com.reclycer.repertoire

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import java.io.File
import java.util.ArrayList

import com.reclycer.repertoire.data.Contact

/**
 * Created by kyouse on 03/12/17.
 */

class DeleteAdapter : RecyclerView.Adapter<DeleteAdapter.MyViewHolder>() {

    private var contact: List<Contact>? = ArrayList()
    private val list_idContact = ArrayList<Int>()

    fun deleteContact(context: Context) {
        val databaseManager = DatabaseManager(context)
        for (i in list_idContact) {
            for (c in contact!!) {
                if (i == c.idContact && c.getisPhotoFromApp()!!) {
                    val mydir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val myImage = File(mydir, c.photoPath!!)
                    myImage.delete()
                    Log.i("DeleteAdapter", "Image : " + c.photoPath!!)
                }
            }
            databaseManager.deleteContact(i)
        }
        databaseManager.close()
    }

    fun refreshContact(context: Context) {
        val databaseManager = DatabaseManager(context)
        contact = databaseManager.readContact()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return contact!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.todelete, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeleteAdapter.MyViewHolder, position: Int) {
        val fouet = contact!![position]
        holder.display(fouet)
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
                    list_idContact.remove(Integer.valueOf(currentContact!!.idContact))
                } else {
                    checkBox.isChecked = true
                    list_idContact.add(currentContact!!.idContact)
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
