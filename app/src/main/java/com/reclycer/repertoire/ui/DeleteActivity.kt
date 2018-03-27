package com.reclycer.repertoire.ui

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.reclycer.repertoire.R
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager
import kotlinx.android.synthetic.main.activity_main.*

class DeleteActivity : AppCompatActivity() {


    private val deleteAdapter = DeleteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contact_list.layoutManager = LinearLayoutManager(this)
        contact_list.adapter = deleteAdapter

        loadData()
    }

    private fun loadData() {
        val databaseManager = DatabaseManager(this)
        deleteAdapter.contactList.addAll(databaseManager.readContactList()!!.toList())
        deleteAdapter.notifyDataSetChanged()
        databaseManager.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.delete_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.Delete_final -> {

                val dataManager = DataManager(this)
                val databaseManager = DatabaseManager(this)
                for (id in deleteAdapter.listIdsToRemove) {
                    databaseManager.markToDelete(id)
                    dataManager.deleteApiContact(id)
                }
                databaseManager.close()

                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
