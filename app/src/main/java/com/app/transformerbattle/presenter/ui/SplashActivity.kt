package com.app.transformerbattle.presenter.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import com.app.transformerbattle.R
import com.app.transformerbattle.databinding.ActivitySplashBinding
import com.app.transformerbattle.repository.AppSharedPrefs
import com.app.transformerbattle.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.math.log

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SplashActivity:  AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    private lateinit var binding: ActivitySplashBinding
    private lateinit var preference: AppSharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preference = AppSharedPrefs(this)
        subscribeObservers()
        viewModel.setStateEvent(SplashStateEvent.GetToken)
    }

    private fun subscribeObservers(){
        if (!preference.getStoredTag(AppSharedPrefs.tokenPref).isNullOrEmpty()){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            viewModel.Token.observe(this, ::handleLiveData)
        }
    }

    private fun handleLiveData(status: Status<String>?) {
        when(status){
            is Status.Success<String> -> setPreference(status.data)
//            is Status.Error -> Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
        }
    }

    private fun setPreference(data: String) {
        Log.e("TAG", "data: ${data}")

        preference.setStoredTag(AppSharedPrefs.tokenPref,data)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}