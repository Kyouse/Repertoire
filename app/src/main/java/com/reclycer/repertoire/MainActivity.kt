package com.reclycer.repertoire

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem

import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val ma = MyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)
        ma.refreshContact(this)
        contact_list.layoutManager = LinearLayoutManager(this)
        contact_list.adapter = ma

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_add -> {
                val intent = Intent(this, AjoutContact::class.java)
                startActivityForResult(intent, 1)

                return true
            }
            R.id.Delete -> {
                val intent1 = Intent(this, DeleteActivity::class.java)
                startActivityForResult(intent1, 2)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    ma.refreshContact(this)
                }
            }
            2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    ma.refreshContact(this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        ma.refreshContact(this)
    }
}
