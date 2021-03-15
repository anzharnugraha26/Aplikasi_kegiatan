package com.example.aplikasikegiatan.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasikegiatan.R
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setupActionBar()
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
}