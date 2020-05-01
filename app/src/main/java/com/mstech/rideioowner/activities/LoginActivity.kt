package com.mstech.rideioowner.activities

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.*
import com.mstech.rideioowner.R
import com.mstech.rideioowner.model.LoginResponse
import com.mstech.rideioowner.model.SharedKey
import com.mstech.rideioowner.utils.MyUtils
import com.mstech.rideioowner.utils.RetrofitFactory
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getBackButton().visibility= View.GONE
        getHomeButton().visibility= View.GONE
        setScreenTitle("Owner Login")
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
                        LogUtils.e(data?.get(0))
                        try{
                            SPStaticUtils.put(SharedKey.ISLOGIN,"true")
                            SPStaticUtils.put(SharedKey.ADDRESS,data?.get(0)?.Address)
                            SPStaticUtils.put(SharedKey.EMAIL,data?.get(0)?.Email)
                            SPStaticUtils.put(SharedKey.FIRSTNAME,data?.get(0)?.FirstName)
                            SPStaticUtils.put(SharedKey.LASTNAME,data?.get(0)?.LastName)
                            SPStaticUtils.put(SharedKey.MOBILE,data?.get(0)?.Mobile)
                            SPStaticUtils.put(SharedKey.OWNER_ID,data?.get(0)?.OwnerId)
//                            SPStaticUtils.put(SharedKey.OWNER_ID,"85")
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
