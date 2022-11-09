package com.mstech.rideioowner.activities
import com.mstech.rideioowner.R
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout


abstract class BaseActivity : AppCompatActivity() {
    lateinit var progressDialog: ProgressDialog
    lateinit var mTextViewScreenTitle: TextView
    lateinit var mImageButtonBack: ImageButton
    lateinit var mImageButtonHome: ImageButton
    lateinit var mImageButtonLogout: TextView
    lateinit var mProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Loading")
        mProgressDialog.setCancelable(false)
        mProgressDialog.isIndeterminate = true
    }

    override fun setContentView(layoutResID: Int) {
        var coordinatorLayout: CoordinatorLayout =
            layoutInflater.inflate(R.layout.activity_base, null) as CoordinatorLayout
        var activityContainer: FrameLayout = coordinatorLayout.findViewById(R.id.layout_container)
        mTextViewScreenTitle = coordinatorLayout.findViewById(R.id.text_screen_title) as TextView
        mImageButtonBack = coordinatorLayout.findViewById(R.id.image_back_button)
        mImageButtonHome = coordinatorLayout.findViewById(R.id.image_home_button)
        mImageButtonLogout = coordinatorLayout.findViewById(R.id.image_logout_button)

        layoutInflater.inflate(layoutResID, activityContainer, true)

        super.setContentView(coordinatorLayout)
    }

    fun setScreenTitle(resId: Int) {
        mTextViewScreenTitle.text = getString(resId)
    }

    fun setScreenTitle(title: String) {
        mTextViewScreenTitle.text = title
    }

    fun getBackButton(): ImageButton {
        return mImageButtonBack
    }
    fun getHomeButton(): ImageButton {
        return mImageButtonHome
    }
    fun getLogoutButton(): TextView {
        return mImageButtonLogout
    }

    fun showProgressDialog() {
        if (!mProgressDialog.isShowing) {
            mProgressDialog.show()
        }
    }

    fun dismissProgressDialog() {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }

    fun showProgress(context: Context, isShowing: Boolean) {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
