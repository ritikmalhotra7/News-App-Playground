package com.complete.newsreporter.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.complete.newsreporter.R
import com.complete.newsreporter.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val mAuth = Constants.firebaseAuth.currentUser
        if (mAuth != null) {
            val i = Intent(this, NewsActivity::class.java)
            i.putExtra("email",mAuth.email.toString())
            i.putExtra("uid",mAuth.uid.toString())
            startActivity(i)
            finish()
        } else {
            Log.d("taget","null")
            super.onStart()
        }
    }

    override fun onStart() {
        super.onStart()

    }
}