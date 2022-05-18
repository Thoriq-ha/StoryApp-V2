package com.thor.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.thor.storyapp.R
import com.thor.storyapp.data.preferences.datastore.DataStoreSession
import com.thor.storyapp.ui.main.MainActivity
import org.koin.android.ext.android.inject

class AuthActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private val session: DataStoreSession by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session.userFlow.asLiveData().observe(this) {
            if (!it.token.isNullOrEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                setContentView(R.layout.activity_auth)
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}