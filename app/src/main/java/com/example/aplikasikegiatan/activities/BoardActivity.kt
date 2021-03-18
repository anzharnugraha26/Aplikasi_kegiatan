package com.example.aplikasikegiatan.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasikegiatan.R
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.android.synthetic.main.activity_my_profile.*

class BoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        setupActionBar()
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
}