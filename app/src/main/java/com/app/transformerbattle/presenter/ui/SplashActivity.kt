package com.app.transformerbattle.presenter.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.transformerbattle.databinding.ActivitySplashBinding
import com.app.transformerbattle.presenter.utils.TransformerEvents
import com.app.transformerbattle.repository.AppSharedPrefs
import com.app.transformerbattle.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

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
        viewModel.onTriggerEvent(TransformerEvents.GetToken)
    }

    private fun subscribeObservers(){
        if (!preference.getStoredTag(AppSharedPrefs.tokenPref).isNullOrEmpty()){
            openMainActivity()
        }else{
            viewModel.Token.observe(this@SplashActivity, ::handleLiveData)
        }
    }

    private fun handleLiveData(status: Status<String>?) {
        when(status){
            is Status.Success<String> -> setPreference(status.data)
        }
    }

    private fun setPreference(data: String) {
        preference.setStoredTag(AppSharedPrefs.tokenPref,data)
        openMainActivity()
    }

    private fun openMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}