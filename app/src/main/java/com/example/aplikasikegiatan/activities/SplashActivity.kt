package com.example.aplikasikegiatan.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.TextView
import com.example.aplikasikegiatan.R
import com.example.aplikasikegiatan.firebase.FirestoreClass

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val typeface: Typeface = Typeface.createFromAsset(assets, "carbon phyber.ttf")
        val tv_name = findViewById<TextView>(R.id.tv_appname)
        tv_name.typeface = typeface

        Handler().postDelayed({
            var currentUser = FirestoreClass().getCurrentUserId()
            if (currentUser.isNotEmpty()) {
                startActivity(Intent(this, DashboardActivity::class.java))
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()

        }, 2500)

    }

}