package com.example.aplikasikegiatan.activities


import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.aplikasikegiatan.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_base.tv_progress_text
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPress = false
    private lateinit var mProggresDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

    }

    fun showProgressDialog(text: String) {
        mProggresDialog = Dialog(this)
        mProggresDialog.setContentView(R.layout.dialog_progress)
        mProggresDialog.tv_progress.text = text
        mProggresDialog.show()
    }

    fun hideProggresDialog() {
        mProggresDialog.dismiss()
    }

    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun doubleToExits() {
        if (doubleBackToExitPress) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPress = true
        Toast.makeText(this, "please", Toast.LENGTH_LONG).show()

        Handler().postDelayed({ doubleBackToExitPress = false }, 2000)

    }

    fun showError(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.ColorPrimaryDark))
        snackBar.show()
    }


}