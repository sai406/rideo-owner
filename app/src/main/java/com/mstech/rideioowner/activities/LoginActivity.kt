package com.mstech.rideioowner.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.*
import com.mstech.rideioowner.R
import com.mstech.rideioowner.model.LoginResponse
import com.mstech.rideioowner.model.SharedKey
import com.mstech.rideioowner.utils.MyUtils
import com.mstech.rideioowner.utils.RetrofitFactory
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title= ("Owner Login")
        login_btn.setOnClickListener(View.OnClickListener {  v ->
            if (!NetworkUtils.isConnected()){
               ToastUtils.showShort("No Internet Connection")
            }else if (StringUtils.isEmpty(mobile.text)){
                ToastUtils.showShort("Enter Mobile Number")
            }else if (StringUtils.isEmpty(pin.text)){
                ToastUtils.showShort("Enter Pin")
            }else{
                validateLogin()
            }


        })
    }

    private fun validateLogin() {
        MyUtils.showProgress(this,true)
        RetrofitFactory.client.loginMethod(mobile.text.toString(),pin.text.toString(),"2288").enqueue(object :
            Callback<List<LoginResponse>>{
            override fun onResponse(call: Call<List<LoginResponse>>, response: Response<List<LoginResponse>>) {
                MyUtils.showProgress(this@LoginActivity,false)
                if (response.isSuccessful){
                    var data = response?.body()
                    if(data?.get(0)?.OwnerId.equals(null)){
                        ToastUtils.showShort("Invalid Login")
                    }else{
                        try{
                            SPStaticUtils.put(SharedKey.ISLOGIN,"true")
                            SPStaticUtils.put(SharedKey.ADDRESS,data?.get(0)?.Address)
                            SPStaticUtils.put(SharedKey.EMAIL,data?.get(0)?.Email)
                            SPStaticUtils.put(SharedKey.FIRSTNAME,data?.get(0)?.FirstName)
                            SPStaticUtils.put(SharedKey.LASTNAME,data?.get(0)?.LastName)
                            SPStaticUtils.put(SharedKey.MOBILE,data?.get(0)?.Mobile)
                            SPStaticUtils.put(SharedKey.OWNER_ID,data?.get(0)?.OwnerId)
                            LogUtils.e(data?.get(0).toString())
                            ActivityUtils.startActivity(HomeActivity::class.java)
                            finish()
                        }catch (e:Exception){

                        }

                    }

                }
            }
            override fun onFailure(call: Call<List<LoginResponse>>, t: Throwable) {
                MyUtils.showProgress(this@LoginActivity,false)
                LogUtils.e(t.message)
            }

        })
    }
}
