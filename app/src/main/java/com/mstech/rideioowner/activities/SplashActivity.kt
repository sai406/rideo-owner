package com.mstech.rideioowner.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.blankj.utilcode.util.SPStaticUtils
import com.mstech.rideioowner.model.SharedKey
import com.mstech.rideioowner.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        Handler().postDelayed({
            if(SPStaticUtils.getString(SharedKey.ISLOGIN,"").equals("true")){
                val mainIntent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(mainIntent)
                finish()
            }else{
                val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(mainIntent)
                finish()
            }

        }, 100)
    }
}
