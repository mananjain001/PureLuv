package com.mananjain.Purelove.Common.LoginSignup

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.mananjain.Purelove.R
import java.io.ByteArrayOutputStream
import java.util.*

class UpdateProfile : AppCompatActivity() {

    val REQUEST_CODE = 1
    var mImageURI: Uri? = null
    lateinit var city: String
    lateinit var phonenumber: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        phonenumber = intent.getStringExtra("PhoneNumber")
        getlocation()
    }

    fun back(view: View) {
        if (validatefields()) {
            showCustomDialog()
        }
    }

    fun save(view: View) {
        if (validatefields()) {
            callnextscreen()
        }
    }

    private fun validatefields(): Boolean {
        val bio = findViewById<TextInputLayout>(R.id.UPBio)
        when (mImageURI) {
            null -> {
                Toast.makeText(this, "Select Profile Picture", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> when {
                bio.editText!!.text.toString().trim().isEmpty() -> {
                    bio.error = "Field can not be empty"
                    return false
                }
                else -> {
                    bio.error = null
                    bio.isErrorEnabled = false
                    return true
                }
            }
        }
    }

    private fun callnextscreen() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_bar_layout)
        var dialog: AlertDialog = builder.create()
        dialog.show()

        val bio = findViewById<TextInputLayout>(R.id.UPBio)
        val biostr = bio.editText!!.text.toString().trim()
        val databasereferance: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child("Accounts")
        val storagereferance: StorageReference =
            FirebaseStorage.getInstance().getReference("ProfilePictures")
        storagereferance.child(phonenumber)
            .putBytes(getfilecompressed(mImageURI))
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                taskSnapshot!!.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task: Task<Uri> ->
                    databasereferance.child(phonenumber).child("profilePicture")
                        .setValue(task.result.toString())
                }
                databasereferance.child(phonenumber).child("bio").setValue(biostr)
                databasereferance.child(phonenumber).child("city").setValue(city)
                startActivity(Intent(this@UpdateProfile, LoginSignupStartupScreen::class.java))
                finishAffinity()
            }
            .addOnFailureListener { exception: Exception ->
                dialog.dismiss()
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    fun cityselection(view: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to change City").setCancelable(true)
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            getlocation()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }

    private fun showCustomDialog() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure?")
        builder.setMessage("Do you want to Save changes").setCancelable(true)
        builder.setPositiveButton("Save") { dialogInterface: DialogInterface, i: Int ->
            callnextscreen()
        }
        builder.setNegativeButton("Discard") { dialogInterface: DialogInterface, i: Int ->
            finish()
        }
        builder.show()

    }

    fun getfilecompressed(uri: Uri?): ByteArray {
        var bitmap: Bitmap? = null
        bitmap = MediaStore.Images.Media.getBitmap(application.contentResolver, uri)
        var baos: ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data: ByteArray = baos.toByteArray()
        return data
    }

    fun getfileextension(uri: Uri): String {
        val cr: ContentResolver = contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri)).toString()
    }

    fun choosefile(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            mImageURI = data.data!!
            val imageView: ImageView = findViewById(R.id.profileimage)
            val imageselectorbutton: Button = findViewById(R.id.profileselectorbutton)
            imageselectorbutton.visibility = View.GONE
            imageView.visibility = View.VISIBLE
            imageView.setImageURI(mImageURI)
        }
    }

    fun changepicture(view: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to change picture").setCancelable(true)
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            val imageView: ImageView = findViewById(R.id.profileimage)
            val imageselectorbutton: Button = findViewById(R.id.profileselectorbutton)
            imageselectorbutton.visibility = View.VISIBLE
            imageView.visibility = View.GONE
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
        }
        builder.show()
    }

    private fun getlocation() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        } else {
            getcurrentlocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getcurrentlocation()
            } else {
                Toast.makeText(this, "Permission Denied! ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getcurrentlocation() {
        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.setInterval(10000)
        locationRequest.setFastestInterval(3000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val cityTextview: TextView = findViewById(R.id.citytextview)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@UpdateProfile)
                        .removeLocationUpdates(this)
                    if (locationResult != null && locationResult.locations.size > 0) {
                        val latestLocationIndex = locationResult.locations.size - 1
                        val latitude = locationResult.locations.get(latestLocationIndex).latitude
                        val longitude = locationResult.locations.get(latestLocationIndex).longitude
                        try {
                            val geocoder: Geocoder =
                                Geocoder(applicationContext, Locale.getDefault())
                            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                            if (addresses != null && addresses.size > 0) {
                                city = addresses.get(0).locality
                                cityTextview.text = city
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }, Looper.getMainLooper())
    }
}