package com.example.facebookclone.utils

import android.content.Context
import android.content.SharedPreferences

class PrefSingleton(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences("main", Context.MODE_PRIVATE)

    fun saveString(key: String?, value: String?) {
        val e = preferences.edit()
        e.putString(key, value)
        e.apply()
    }

    fun getString(key: String?): String {
        return preferences.getString(key, "") ?: ""
    }

    fun saveInt(key: String?, value: Int) {
        val e = preferences.edit()
        e.putInt(key, value)
        e.apply()
    }

    fun getInt(key: String?): Int {
        return preferences.getInt(key, 0)
    }

    fun saveBool(key: String?, value: Boolean) {
        val e = preferences.edit()
        e.putBoolean(key, value)
        e.apply()
    }

    fun getBool(key: String?): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

//    companion object {
//        private var mInstance: PrefSingleton? = null
//        val instance: PrefSingleton?
//            get() {
//                if (mInstance == null) mInstance = PrefSingleton(context = )
//                return mInstance
//            }
//    }
}