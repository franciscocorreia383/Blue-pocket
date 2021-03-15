package com.example.bluepocket.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.bluepocket.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

            val handle = Handler()
        handle.postDelayed({
            startActivity(Intent(this,LoginActivity::class.java))
        },3000)


    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}