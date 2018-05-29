package com.reclycer.repertoire.ui.edit

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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.reclycer.repertoire.R
import com.reclycer.repertoire.data.Contact
import kotlinx.android.synthetic.main.activity_update_contact.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


abstract class BaseEditContactActivity : AppCompatActivity() {


    private var mCurrentPhotoPath: String? = null
    private var tempPhotoPath: String? = null
    private var LastPhotoPath: String? = null
    private var photoFromApp: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_contact)


//        valider!!.setOnClickListener(View.OnClickListener { view ->
//            onValidButtonClicked()
//
//        })


        takepicbutton!!.setOnClickListener { view ->
            onTakePictureButton(view)
        }


        importbutton!!.setOnClickListener { view ->
//            onImportPictureButton(view, activity)
        }
    }

    private fun onImportPictureButton(view: View, activity: AddContactActivity) {
        val import_intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (ActivityCompat.checkSelfPermission(view.context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMS_CALL_ID)
        } else
            startActivityForResult(import_intent, REQUEST_LOAD_IMAGE)
    }

    private fun onTakePictureButton(view: View) {
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

    private fun onValidButtonClicked() {
        if (editFirstName!!.text.isEmpty()) {
            Toast.makeText(this, "Vous n'avez pas écrit le prénom", Toast.LENGTH_SHORT).show()
            invalidateOptionsMenu()
            return
        }

        if (editLastName!!.text.isEmpty()) {
            Toast.makeText(this, "Vous n'avez pas écrit le lastName", Toast.LENGTH_SHORT).show()
            invalidateOptionsMenu()
            return
        }

        if (editPhoneNumber!!.text.toString().trim { it <= ' ' }.length < 3) {
            Toast.makeText(this, "Le numéro doit être composé au minimum de 3 chiffres", Toast.LENGTH_SHORT).show()
            invalidateOptionsMenu()
            return
        }

        val contact = Contact(
                editLastName!!.text.toString(),
                editFirstName!!.text.toString(),
                editPhoneNumber!!.text.toString()

        )

        if (LastPhotoPath != null)
            contact.photoPath = LastPhotoPath
        contact.setisPhotoFromApp(photoFromApp)

        save(contact)
    }

    protected abstract fun save(contact: Contact)


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.ajout_contact_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.addContactButton-> {
                item.actionView = ProgressBar(this)
                onValidButtonClicked()


                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
                    photo_preview!!.setImageURI(Uri.fromFile(File(LastPhotoPath!!)))
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

                    photo_preview!!.setImageURI(Uri.fromFile(File(LastPhotoPath!!)))
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
