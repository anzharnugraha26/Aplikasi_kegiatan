package com.example.aplikasikegiatan.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.aplikasikegiatan.R
import com.example.aplikasikegiatan.firebase.FirestoreClass
import com.example.aplikasikegiatan.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        settupActionBar()
    }

    private fun settupActionBar() {
        setSupportActionBar(tool_bar_singup)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        tool_bar_singup.setNavigationOnClickListener {
            onBackPressed()
        }

        submit.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val name: String = tv_username.text.toString().trim { it <= ' ' }
        val email: String = tv_email.text.toString().trim { it <= ' ' }
        val password: String = tv_password.text.toString().trim { it <= ' ' }


        if (validateForm(name, email, password)) {
//            Toast.makeText(this,"sukses", Toast.LENGTH_SHORT).show()
            showProgressDialog("please wait")
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProggresDialog()
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registerEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, registerEmail)
                        FirestoreClass().regiterUser(this, user)
                    } else {
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showError("please enter a name")
                false
            }
            TextUtils.isEmpty(email) -> {
                showError("please enter a email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showError("please enter a password")
                false
            }
            else -> {
                true
            }

        }

    }


    fun userResgisteredSucess() {
        Toast.makeText(this, " sukses ", Toast.LENGTH_SHORT)
            .show()
        hideProggresDialog()
        FirebaseAuth.getInstance().signOut()
        finish()

    }


}