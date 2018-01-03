package com.reclycer.repertoire;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list) RecyclerView rv;
    private MyAdapter ma = new MyAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        ma.refreshContact(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(ma);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

           return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add:
                Intent intent = new Intent(this, AjoutContact.class);
                startActivityForResult(intent, 1);

                return true;
            case R.id.Delete:
                Intent intent1 = new Intent(this, DeleteActivity.class);
                startActivityForResult(intent1, 2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (1): {
                if (resultCode == Activity.RESULT_OK) {
                    ma.refreshContact(this);
                }
                break;
            }
            case(2): {
                if (resultCode == Activity.RESULT_OK) {
                    ma.refreshContact(this);
                    }
                }
                break;
        }
    }
 }
