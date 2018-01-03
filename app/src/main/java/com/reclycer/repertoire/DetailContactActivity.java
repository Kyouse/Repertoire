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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class DetailContactActivity extends AppCompatActivity {
    private Contact currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        int idContact;
        final Activity activity = this;
        ImageView photo = (ImageView) findViewById(R.id.imageView2);
        ImageButton phoneButton = (ImageButton) findViewById(R.id.phoneButton);
        ImageButton smsButton = (ImageButton) findViewById(R.id.smsButton);
        TextView nom = (TextView) findViewById(R.id.nom_detail);
        TextView prenom = (TextView) findViewById(R.id.prenom_detail);
        TextView numero = (TextView) findViewById(R.id.numero_detail);

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
}
