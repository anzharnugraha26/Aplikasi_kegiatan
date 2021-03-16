package com.example.aplikasikegiatan.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.aplikasikegiatan.R
import com.example.aplikasikegiatan.firebase.FirestoreClass
import com.example.aplikasikegiatan.models.User
import com.example.aplikasikegiatan.utils.Constans
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.IOException


class MyProfileActivity : BaseActivity() {

    companion object {
        private const val READ_STORAGE_PERMISION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserDetails: User
    private var mProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setupActionBar()
        FirestoreClass().loadDataUser(this)
        profile_user_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChosser()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISION_CODE
                )
            }
        }

        btn_update.setOnClickListener {
            if (mSelectedImageFileUri != null) {
                uploadUserImage()
            } else {
                showProgressDialog("please")
                updateUserProfileData()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChosser()
            }
        } else {
            Toast.makeText(this, "OOps", Toast.LENGTH_LONG).show()
        }
    }

    private fun showImageChosser() {
        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            mSelectedImageFileUri = data.data
            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_arrow_back_ios_24)
                    .into(profile_user_image)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }

    private fun setupActionBar() {
        setSupportActionBar(tool_bar_profile)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            actionBar.title = "My Profile"
        }
        tool_bar_profile.setNavigationOnClickListener {
            onBackPressed()
        }

    }


    fun setUserDataInUI(user: User) {
        mUserDetails = user
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_arrow_back_ios_24)
            .into(profile_user_image)

        profile_email.setText(user.email)
        profile_username.setText(user.name)
        if (user.mobile != 0L) {
            profile_mobile.setText(user.mobile.toString())
        }
    }

    private fun updateUserProfileData() {
        val userHashMap = HashMap<String, Any>()

        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image) {
            userHashMap[Constans.IMAGE] = mProfileImageURL
        }
        if (profile_username.toString() != mUserDetails.name) {
            userHashMap[Constans.NAME] = profile_username.text.toString()
        }
        if (profile_mobile.toString() != mUserDetails.mobile.toString()) {
            userHashMap[Constans.MOBILE] = profile_mobile.text.toString().toLong()
        }

        FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    private fun uploadUserImage() {
        showProgressDialog("Please Wait")
        if (mSelectedImageFileUri != null) {
            val sRef: StorageReference =
                FirebaseStorage.getInstance().reference.child(
                    "USER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(
                        mSelectedImageFileUri
                    )
                )
            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener { taskSnapsot ->
                Log.i(
                    "Firebase Image URL",
                    taskSnapsot.metadata!!.reference!!.downloadUrl.toString()
                )
                taskSnapsot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    Log.i("Downrload Image URL", uri.toString())
                    mProfileImageURL = uri.toString()
                    hideProggresDialog()

                    updateUserProfileData()
                }
            }.addOnFailureListener { exeption ->
                Toast.makeText(this, exeption.message, Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun getFileExtension(uri: Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    fun profileUpdateSuccess() {
        hideProggresDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }


}