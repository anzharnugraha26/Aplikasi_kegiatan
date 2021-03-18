package com.example.aplikasikegiatan.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.aplikasikegiatan.R
import com.example.aplikasikegiatan.firebase.FirestoreClass
import com.example.aplikasikegiatan.models.Board
import com.example.aplikasikegiatan.utils.Constans
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.IOException

class BoardActivity : BaseActivity() {
    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserName: String
    private var mBoardImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        setupActionBar()

        if (intent.hasExtra(Constans.NAME)) {
            mUserName = intent.getStringExtra(Constans.NAME).toString()
        }


        image_board.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Constans.showImageChosser(this)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constans.READ_STORAGE_PERMISION_CODE
                )
            }
        }

        btn_create_board.setOnClickListener {
            if (mSelectedImageFileUri != null){
                uploadBoardImage()
            } else {
                showProgressDialog("please wait")
                createBoard()
            }
        }

    }


    private fun createBoard() {
        val assigenedUsersArrayList: ArrayList<String> = ArrayList()
        assigenedUsersArrayList.add(getCurrentUserID())

        var board = Board(
            tv_board.text.toString(),
            mBoardImageURL,
            mUserName,
            assigenedUsersArrayList
        )

        FirestoreClass().createdBoard(this, board)
    }


    private fun uploadBoardImage() {
        showProgressDialog("please wait")
        val sRef: StorageReference =
            FirebaseStorage.getInstance().reference.child(
                "BOARD_IMAGE" + System.currentTimeMillis() + "." + Constans.getFileExtension(
                    this,
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
                mBoardImageURL = uri.toString()
                hideProggresDialog()

                createBoard()
            }
        }.addOnFailureListener { exeption ->
            Toast.makeText(this, exeption.message, Toast.LENGTH_LONG).show()

        }
    }

    fun boardCreatedSuccessfully() {
        hideProggresDialog()
        finish()
    }


    private fun setupActionBar() {
        setSupportActionBar(tool_bar_board)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            actionBar.title = "My Board"
        }
        tool_bar_board.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constans.READ_STORAGE_PERMISION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constans.showImageChosser(this)
            }
        } else {
            Toast.makeText(this, "OOps", Toast.LENGTH_LONG).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constans.PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            mSelectedImageFileUri = data.data
            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_arrow_back_ios_24)
                    .into(image_board)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }


}