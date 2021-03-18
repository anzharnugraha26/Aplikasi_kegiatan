package com.example.aplikasikegiatan.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.aplikasikegiatan.R
import com.example.aplikasikegiatan.utils.Constans
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.IOException

class BoardActivity : BaseActivity() {
    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        setupActionBar()

        if (intent.hasExtra(Constans.NAME)){
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

    }

    fun boardCreatedSuccessfully(){
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