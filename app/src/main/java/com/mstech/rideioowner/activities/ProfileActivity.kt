package com.mstech.rideioowner.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.SPStaticUtils
import com.mstech.rideioowner.R
import com.mstech.rideioowner.model.SharedKey


class ProfileActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_notifications)
        setScreenTitle("Profile")
        getBackButton().setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        getHomeButton().setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        })
        val name: TextView = findViewById(R.id.name)
        val mobile: TextView = findViewById(R.id.mobile)
        val email: TextView = findViewById(R.id.email)
        val address: TextView = findViewById(R.id.address)
        name.text = SPStaticUtils.getString(SharedKey.FIRSTNAME,"")+" "+ SPStaticUtils.getString(
            SharedKey.LASTNAME,"")
        mobile.text = SPStaticUtils.getString(SharedKey.MOBILE,"")
        email.text = SPStaticUtils.getString(SharedKey.EMAIL,"")
        address.text = SPStaticUtils.getString(SharedKey.ADDRESS,"")

    }
}
