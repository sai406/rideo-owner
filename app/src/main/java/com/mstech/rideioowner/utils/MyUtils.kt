package com.mstech.rideioowner.utils

import android.app.ProgressDialog
import android.content.Context
import com.mstech.rideioowner.R
import java.lang.Exception


object MyUtils {
   lateinit  var progressDialog: ProgressDialog
    fun showProgress(context: Context ,isShowing: Boolean) {
        try {
            if (isShowing == true) {
                progressDialog = ProgressDialog(context)
                progressDialog.setCancelable(false)
                progressDialog.show()
                progressDialog.setContentView(R.layout.progress_layout)
                progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);

            } else if (isShowing == false) {
                progressDialog.dismiss()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

}