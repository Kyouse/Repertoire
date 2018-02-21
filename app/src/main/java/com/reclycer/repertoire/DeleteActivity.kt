package com.reclycer.repertoire

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class DeleteActivity : AppCompatActivity() {


    private val deleteAdapter = DeleteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        deleteAdapter.refreshContact(this)
        contact_list.layoutManager = LinearLayoutManager(this)
        contact_list.adapter = deleteAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.delete_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.Delete_final -> {
                deleteAdapter.markToDelete(this)
//                deleteAdapter.deleteContact(this)


                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
