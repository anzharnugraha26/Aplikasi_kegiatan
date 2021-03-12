package com.example.aplikasikegiatan.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.aplikasikegiatan.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        auth = FirebaseAuth.getInstance()
        settupActionBar()
        sign_in.setOnClickListener {
            signInUser()
        }
    }

    private fun settupActionBar() {
        setSupportActionBar(tool_bar_singin)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        tool_bar_singin.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun signInUser() {
        val email: String = tv_email.text.toString().trim { it <= ' ' }
        val password: String = tv_password.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            showProgressDialog("please")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success")
                        val user = auth.currentUser
                       startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                        // ...
                    }

                    // ...
                }


        }


    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
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


}