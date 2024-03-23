package com.dicoding.mysharedpreferences.util

import android.content.Context
import com.dicoding.mysharedpreferences.model.UserModel

internal class UserPreference(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // setter
    fun setUser(value: UserModel) { // setiap kali set User di panggil dia akan set data dari argument UserModel yang terlah di set sebelumnya
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(EMAIL, value.email)
        editor.putInt(AGE, value.age)
        editor.putString(PHONE_NUMBER, value.phoneNumber)
        editor.putBoolean(LOVE_MU, value.isLove)
        editor.apply() // ini untuk menyimpanya, ada juga commit yang berjalan secara synchronus, sedangkan apply() berjalan dengan Asysnchronus
    }

    // getter
    fun getUser(): UserModel { // setiap kali getUser di panggil dia akan mengambalikan data class userModel yang sudah di tambahkan datanya
        val model = UserModel()
        model.name = preferences.getString(NAME, "") // mengambil value di preference dan menerapkanya pada model, terdapat default falue juga
        model.email = preferences.getString(EMAIL, "")
        model.age = preferences.getInt(AGE, 0)
        model.phoneNumber = preferences.getString(PHONE_NUMBER, "")
        model.isLove = preferences.getBoolean(LOVE_MU, false)

        return model
    }

    // Ini untuk mempersiapkan key nya, selain ini juga bisa dengan menyimpanya pada string.xml
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val AGE = "age"
        private const val PHONE_NUMBER = "phone"
        private const val LOVE_MU = "islove"
    }
}