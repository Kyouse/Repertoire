package com.reclycer.repertoire.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import com.bumptech.glide.Glide
import com.reclycer.repertoire.R

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

import com.reclycer.repertoire.data.Contact
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager

class ModifContact : AppCompatActivity() {
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
    private lateinit var currentContact: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modif_contact)

        val mIntent = intent
        val idContact = mIntent.getIntExtra("IdContact", 0)

        databaseManager = DatabaseManager(this)
        val activity = this

        Log.i("idContact", idContact.toString())
        currentContact = databaseManager!!.getContact(idContact)?:throw RuntimeException("IdContact introuvable")

        prenom = findViewById(R.id.modif_first_name) as EditText
        nom = findViewById(R.id.modif_Name) as EditText
        numero = findViewById(R.id.modif_Numero) as EditText
        previewpicture = findViewById(R.id.modif_photo_preview) as ImageView
        valider = findViewById(R.id.modif_valider) as Button
        takepic = findViewById(R.id.modif_takepicbutton) as Button
        importpic = findViewById(R.id.modif_importbutton) as Button
        photoFromApp = false

        prenom!!.setText(currentContact!!.prenom)
        nom!!.setText(currentContact!!.nom)
        numero!!.setText(currentContact!!.numero)


        valider!!.setOnClickListener(View.OnClickListener { view ->
            if (prenom!!.text.toString().trim { it <= ' ' }.length == 0) {
                Toast.makeText(view.context, "Vous n'avez pas écrit le prénom", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (nom!!.text.toString().trim { it <= ' ' }.length == 0) {
                Toast.makeText(view.context, "Vous n'avez pas écrit le nom", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (numero!!.text.toString().trim { it <= ' ' }.length < 3) {
                Toast.makeText(view.context, "Le numéro doit être composé au minimum de 3 chiffres", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            currentContact!!.prenom = prenom!!.text.toString()
            currentContact!!.nom = nom!!.text.toString()
            currentContact!!.numero = numero!!.text.toString()

            if (LastPhotoPath != null) {
                if (currentContact!!.getisPhotoFromApp()!!) {
                    val mydir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val myImage = File(mydir, currentContact!!.photoPath!!)
                    myImage.delete()
                }
                currentContact!!.photoPath = LastPhotoPath
                currentContact!!.setisPhotoFromApp(photoFromApp)
            }

            databaseManager!!.updateContact(currentContact)
            databaseManager!!.close()

            val dataManager = DataManager(this)
            dataManager.updateContact(currentContact)

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
                    Glide.with(this)
                            .load(LastPhotoPath)
                            .centerCrop()
                            .into(previewpicture);
            //        previewpicture!!.setImageURI(Uri.fromFile(File(LastPhotoPath!!)))
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
