package com.mstech.rideioowner.utils
import android.R
import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class TextFont : Application() {
    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("RobotoCondensed-Regular.ttf")
                .setFontAttrId(R.attr.fontFamily)
                .build()
        )
    }
}