package com.reclycer.repertoire

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

import com.reclycer.repertoire.data.Contact


class AjoutContact : AppCompatActivity() {
    private var prenom: EditText? = null
    private var nom: EditText? = null
    private var numero: EditText? = null
    private var valider: Button? = null
    private var takepic: Button? = null
    private var importpic: Button? = null
    private var previewpicture: ImageView? = null
    private var databaseManager: DatabaseManager? = null
    private var mCurrentPhotoPath: String? = null
    private var tempPhotoPath: String? = null
    private var LastPhotoPath: String? = null
    private var photoFromApp: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ajoutcontact)
        databaseManager = DatabaseManager(this)
        val activity = this

        prenom = findViewById(R.id.edit_first_name) as EditText
        nom = findViewById(R.id.edit_Name) as EditText
        numero = findViewById(R.id.editNumero) as EditText
        previewpicture = findViewById(R.id.photo_preview) as ImageView
        valider = findViewById(R.id.valider) as Button
        takepic = findViewById(R.id.takepicbutton) as Button
        importpic = findViewById(R.id.importbutton) as Button
        photoFromApp = false

        valider!!.setOnClickListener(View.OnClickListener { view ->
            if (prenom!!.text.isEmpty()) {
                Toast.makeText(view.context, "Vous n'avez pas écrit le prénom", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (nom!!.text.isEmpty()) {
                Toast.makeText(view.context, "Vous n'avez pas écrit le nom", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (numero!!.text.toString().trim { it <= ' ' }.length < 3) {
                Toast.makeText(view.context, "Le numéro doit être composé au minimum de 3 chiffres", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            val contact = Contact(
                    nom!!.text.toString(),
                    prenom!!.text.toString(),
                    numero!!.text.toString()

            )

            if (LastPhotoPath != null)
                contact.photoPath = LastPhotoPath
            contact.setisPhotoFromApp(photoFromApp)

            databaseManager!!.insertContact(contact)
            databaseManager!!.close()

            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        })

        takepic!!.setOnClickListener { view ->
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (takePictureIntent.resolveActivity(packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    Log.e("TakePictureIntent", "can't create image")
                }

                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(view.context,
                            "com.example.repertoire.fileprovider",
                            photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }


        importpic!!.setOnClickListener { view ->
            val import_intent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (ActivityCompat.checkSelfPermission(view.context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMS_CALL_ID)
            } else
                startActivityForResult(import_intent, REQUEST_LOAD_IMAGE)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        mCurrentPhotoPath = image.absolutePath
        Log.i("path", mCurrentPhotoPath)
        return image
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    if (tempPhotoPath != null) {
                        val mydir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        val myImage = File(mydir, tempPhotoPath!!)
                        myImage.delete()
                    }
                    LastPhotoPath = mCurrentPhotoPath
                    tempPhotoPath = mCurrentPhotoPath
                    previewpicture!!.setImageURI(Uri.fromFile(File(LastPhotoPath!!)))
                    photoFromApp = true
                }
                REQUEST_LOAD_IMAGE -> {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        return

                    if (tempPhotoPath != null) {
                        val mydir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        val myImage = File(mydir, tempPhotoPath!!)
                        myImage.delete()
                    }
                    tempPhotoPath = null

                    val selectedImageURI = data?.data
                    LastPhotoPath = getRealPathFromURI(selectedImageURI)
                    Log.i("path", LastPhotoPath)

                    previewpicture!!.setImageURI(Uri.fromFile(File(LastPhotoPath!!)))
                    photoFromApp = false
                }
            }
        }
    }

    private fun getRealPathFromURI(contentURI: Uri?): String {
        val result: String
        val cursor = contentResolver.query(contentURI!!, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(index)
            cursor.close()
        }
        return result
    }

    companion object {
        private val REQUEST_IMAGE_CAPTURE = 1
        private val REQUEST_LOAD_IMAGE = 2
        private val PERMS_CALL_ID = 3
    }
}
