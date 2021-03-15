package com.example.aplikasikegiatan.activities

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.aplikasikegiatan.R
import com.example.aplikasikegiatan.firebase.FirestoreClass
import com.example.aplikasikegiatan.models.User
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MyProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setupActionBar()
        FirestoreClass().loadDataUser(this)
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


    fun setUserDataInUI(user : User){
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_arrow_back_ios_24)
            .into(profile_user_image)

        profile_email.setText(user.email)
        profile_username.setText(user.name)
        if (user.mobile != 0L){
             profile_mobile.setText(user.mobile.toString())
        }
    }
}