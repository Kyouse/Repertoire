package com.reclycer.repertoire

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

import java.util.ArrayList

class DeleteActivity : AppCompatActivity() {


    private val da = DeleteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        da.refreshContact(this)
        contact_list.layoutManager = LinearLayoutManager(this)
        contact_list.adapter = da
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.delete_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.Delete_final -> {
                da.deleteContact(this)

                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
