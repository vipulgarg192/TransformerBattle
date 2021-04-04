package com.app.transformerbattle.repository

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSharedPrefs @Inject constructor(@ApplicationContext context : Context){

    companion object{
        val tokenPref = "AppToken"
    }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getStoredTag(key: String, defValue: String = ""): String {
        return prefs.getString(key, defValue)!!
    }

    fun setStoredTag(key: String,query: String) {
        prefs.edit().putString(key, query).apply()
    }
}