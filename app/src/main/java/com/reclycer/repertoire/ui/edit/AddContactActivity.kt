package com.reclycer.repertoire.ui.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.reclycer.repertoire.data.Contact
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable


class AddContactActivity : BaseEditContactActivity() {

    private var databaseManager: DatabaseManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun save(contact: Contact) {
        databaseManager!!.insertContact(contact)
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
}
