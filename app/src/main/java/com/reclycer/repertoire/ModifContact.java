package com.reclycer.repertoire;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifContact extends AppCompatActivity {
    private EditText prenom, nom, numero;
    private Button valider, takepic, importpic;
    private ImageView previewpicture;
    private DatabaseManager databaseManager;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_LOAD_IMAGE = 2;
    private static final int PERMS_CALL_ID = 3;
    private String mCurrentPhotoPath;
    private String tempPhotoPath;
    private String LastPhotoPath;
    private Boolean photoFromApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif_contact);

        databaseManager = new DatabaseManager(this);
        final Activity activity = this;

        prenom = (EditText) findViewById(R.id.modif_first_name);
        nom = (EditText) findViewById(R.id.modif_Name);
        numero = (EditText) findViewById(R.id.modif_Numero);
        previewpicture = (ImageView) findViewById(R.id.modif_photo_preview);
        valider = (Button) findViewById(R.id.modif_valider);
        takepic = (Button) findViewById(R.id.modif_takepicbutton);
        importpic = (Button) findViewById(R.id.modif_importbutton);
        photoFromApp = false;

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prenom.getText().toString().trim().length() == 0) {
                    Toast.makeText(view.getContext(), "Vous n'avez pas écrit le prénom", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (nom.getText().toString().trim().length() == 0) {
                    Toast.makeText(view.getContext(), "Vous n'avez pas écrit le nom", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (numero.getText().toString().trim().length() < 3) {
                    Toast.makeText(view.getContext(), "Le numéro doit être composé au minimum de 3 chiffres", Toast.LENGTH_SHORT).show();
                    return;
                }

                Contact contact = new Contact(
                        nom.getText().toString(),
                        prenom.getText().toString(),
                        numero.getText().toString()

                );

                if (LastPhotoPath != null)
                    contact.setPhotoPath(LastPhotoPath);
                contact.setisPhotoFromApp(photoFromApp);

                databaseManager.insertContact(contact);
                databaseManager.close();

                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.e("TakePictureIntent", "can't create image");
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(view.getContext(),
                                "com.example.repertoire.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });


        importpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent import_intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMS_CALL_ID);
                } else
                    startActivityForResult(import_intent, REQUEST_LOAD_IMAGE);
            }

        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("path", mCurrentPhotoPath);
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case (REQUEST_IMAGE_CAPTURE): {
                    if (tempPhotoPath != null) {
                        File mydir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File myImage = new File(mydir, tempPhotoPath);
                        myImage.delete();
                    }
                    LastPhotoPath = mCurrentPhotoPath;
                    tempPhotoPath = mCurrentPhotoPath;
                    previewpicture.setImageURI(Uri.fromFile(new File(LastPhotoPath)));
                    photoFromApp = true;
                    break;
                }
                case (REQUEST_LOAD_IMAGE): {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        return;

                    if (tempPhotoPath != null) {
                        File mydir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File myImage = new File(mydir, tempPhotoPath);
                        myImage.delete();
                    }
                    tempPhotoPath = null;

                    Uri selectedImageURI = data.getData();
                    LastPhotoPath = getRealPathFromURI(selectedImageURI);
                    Log.i("path", LastPhotoPath);

                    previewpicture.setImageURI(Uri.fromFile(new File(LastPhotoPath)));
                    photoFromApp = false;
                    break;
                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }
}
