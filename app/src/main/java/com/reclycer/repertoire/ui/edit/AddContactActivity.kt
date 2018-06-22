package com.reclycer.repertoire.ui.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.reclycer.repertoire.data.Contact
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager
import com.reclycer.repertoire.data.fcm.MyRepertoireInstanceIDService
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable


class AddContactActivity : BaseEditContactActivity() {

    private var databaseManager: DatabaseManager? = null
    private var isCurrent: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCurrent = intent.getBooleanExtra(IS_CURRENT, false)
        databaseManager = DatabaseManager(this)

    }

    override fun save(contact: Contact) {

        contact.isCurrent = isCurrent
        Log.i("contact", "$contact")
        databaseManager!!.insertContact(contact)
        if(isCurrent){startService(Intent (this, MyRepertoireInstanceIDService::class.java))}

        databaseManager!!.close()

        val dataManager = DataManager(this)
        dataManager.createContact(contact)
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
                        Toast.makeText(this@AddContactActivity, "Failed to create contact", Toast.LENGTH_SHORT).show()
                    }

                })
    }


    companion object {
        const val IS_CURRENT= "IS_CURRENT"
    }
}
