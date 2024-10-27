package com.example.majujayaaccessories

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Mengubah warna status bar agar sesuai dengan background
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_green)


        // Navigasi ke MainActivity setelah 2 detik
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000) // 2000 ms = 2 detik
    }
}