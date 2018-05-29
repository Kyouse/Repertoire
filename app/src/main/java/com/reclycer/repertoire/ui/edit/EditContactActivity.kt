package com.reclycer.repertoire.ui.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.reclycer.repertoire.data.Contact
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_update_contact.*


@SuppressLint("Registered")
class EditContactActivity : BaseEditContactActivity() {

    private var databaseManager: DatabaseManager? = null
    private var currentContact : Contact? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager(this)
        loadContact()

    }

    override fun save(contact: Contact) {
        currentContact!!.firstName = editFirstName!!.text.toString()
        currentContact!!.lastName = editLastName!!.text.toString()
        currentContact!!.phoneNumber = editPhoneNumber!!.text.toString()

        databaseManager!!.updateContact(currentContact!!)
        databaseManager!!.close()

        val dataManager = DataManager(this)
        dataManager.updateContact(currentContact!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        val resultIntent = Intent()
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onError(e: Throwable?) {
                        invalidateOptionsMenu()
                        Toast.makeText(this@EditContactActivity, "Failed to update contact", Toast.LENGTH_SHORT).show()
                    }

                })
    }

    private fun loadContact() {
        val mIntent = intent
        val idContact = mIntent.getIntExtra("IdContact", 0)
        Log.i("idContact", idContact.toString())
        currentContact = databaseManager!!.getContact(idContact)?:throw RuntimeException("IdContact introuvable")
        editFirstName!!.setText(currentContact!!.firstName)
        editLastName!!.setText(currentContact!!.lastName)
        editNumero!!.setText(currentContact!!.phoneNumber)

    }
}
