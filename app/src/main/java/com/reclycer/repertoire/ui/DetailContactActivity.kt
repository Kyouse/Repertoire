package com.reclycer.repertoire.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import java.io.File
import com.bumptech.glide.Glide
import com.reclycer.repertoire.R
import com.reclycer.repertoire.data.Contact
import com.reclycer.repertoire.data.DatabaseManager
import com.reclycer.repertoire.ui.edit.EditContactActivity
import kotlinx.android.synthetic.main.activity_display_contact.*

class DetailContactActivity : AppCompatActivity() {
//    @BindView(R.id.imageView2) internal var photo: ImageView? = null
//    @BindView(R.id.phoneButton) internal var phoneButton: ImageButton? = null
//    @BindView(R.id.smsButton) internal var smsButton: ImageButton? = null
//    @BindView(R.id.nom_detail) internal var lastName: TextView? = null
//    @BindView(R.id.prenom_detail) internal var firstName: TextView? = null
//    @BindView(R.id.numero_detail) internal var phoneNumber: TextView? = null


    private var currentContact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_contact)


        val idContact: Int
        val activity = this

        val intent = intent
        idContact = intent.getIntExtra("ContactID", -1)

        val databaseManager = DatabaseManager(this)
        currentContact = databaseManager.getContact(idContact)
        databaseManager.close()

        nom_detail!!.text = "${currentContact!!.lastName} ${currentContact!!.firstName}"
        numero_detail!!.text = currentContact!!.phoneNumber
        if (currentContact!!.photoPath != null) {
            val file = File(currentContact!!.photoPath!!)
            if (file.exists())
               // imageView2!!.setImageURI(Uri.fromFile(File(currentContact!!.photoPath!!)))
                Glide.with(this)
                        .load(currentContact!!.photoPath!!)
                        .centerCrop()
                        .into(imageView2);
        }


        phoneButton!!.setOnClickListener(View.OnClickListener { view ->
            val phoneIntent = Intent(Intent.ACTION_CALL)
            phoneIntent.data = Uri.parse("tel:" + currentContact!!.phoneNumber!!)

            if (ActivityCompat.checkSelfPermission(view.context,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return@OnClickListener
            }
            startActivity(phoneIntent)
        })

        smsButton!!.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + currentContact!!.phoneNumber!!))) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_contact_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.edit -> {
                val intent = Intent(this, EditContactActivity::class.java)
                intent.putExtra("IdContact", currentContact!!.idContact)
                startActivity(intent)

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
