package com.nexgencoders.whatsappgb.utils

import android.content.Context
import com.nexgencoders.whatsappgb.R


class PrefHelper(val context: Context) {

    private val PREF_FILE = context.getString(R.string.app_name)
    private val sharedPreferences =
        context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
    fun clearAll(context: Context) {
        val sharedPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.clear()
        editor.apply()
    }

    fun setSharedPreferenceString(key: String?, value: String?) {
        val editor = context.getSharedPreferences(PREF_FILE, 0).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setSharedPreferenceInt(key: String?, value: Int) {
        val editor = context.getSharedPreferences(PREF_FILE, 0).edit()
        editor.putInt(key, value)
        editor.apply()
    }


    fun setSharedPreferenceBoolean(key: String?, value: Boolean) {
        val editor = context.getSharedPreferences(PREF_FILE, 0).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getString(key: String?, defValue: String?): String? {
        return context.getSharedPreferences(PREF_FILE, 0).getString(key, defValue)
    }

    fun getSharedPreferenceInt(key: String?, defValue: Int): Int {
        return context.getSharedPreferences(PREF_FILE, 0).getInt(key, defValue)
    }


    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return context.getSharedPreferences(PREF_FILE, 0).getBoolean(key, defValue)
    }


    fun setSharedPreferenceDouble(key: String?, value: Double) {
        val editor = context.getSharedPreferences(PREF_FILE, 0).edit()
        editor.putString(key, java.lang.Double.doubleToRawLongBits(value).toString())
        editor.apply()
    }

    fun getSharedPreferenceDouble(key: String?, defValue: Double): Double {
        val rawValue = context.getSharedPreferences(PREF_FILE, 0).getString(key, null)
        return if (rawValue != null) {
            java.lang.Double.longBitsToDouble(rawValue.toLong())
        } else {
            defValue
        }
    }



}