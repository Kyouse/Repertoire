package com.reclycer.repertoire;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailContactActivity extends AppCompatActivity {
    @BindView(R.id.imageView2) ImageView photo;
    @BindView(R.id.phoneButton) ImageButton phoneButton;
    @BindView(R.id.smsButton) ImageButton smsButton;
    @BindView(R.id.nom_detail) TextView nom;
    @BindView(R.id.prenom_detail) TextView prenom;
    @BindView(R.id.numero_detail) TextView numero;


    private Contact currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);
        ButterKnife.bind(this);

        int idContact;
        final Activity activity = this;

        Intent intent = getIntent();
        idContact = intent.getIntExtra("ContactID",-1);

        DatabaseManager databaseManager = new DatabaseManager(this);
        currentContact = databaseManager.getContact(idContact);
        databaseManager.close();

        nom.setText(currentContact.getNom());
        prenom.setText(currentContact.getPrenom());
        numero.setText(currentContact.getNumero());
        if(currentContact.getPhotoPath() != null) {
            File file = new File(currentContact.getPhotoPath());
            if(file.exists())
                photo.setImageURI(Uri.fromFile(new File(currentContact.getPhotoPath())));
        }


        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:" + currentContact.getNumero()));

                if (ActivityCompat.checkSelfPermission(view.getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.CALL_PHONE}, 1);
                        return;
                }
                startActivity(phoneIntent);
            }
        });

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                        + currentContact.getNumero())));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_contact_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(this, ModifContact.class);
                intent.putExtra("IdContact", currentContact.getIdContact());
                startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
