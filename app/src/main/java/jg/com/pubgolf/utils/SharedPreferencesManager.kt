package jg.com.pubgolf.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import jg.com.pubgolf.R
import jg.com.pubgolf.data.model.MeResponse
import jg.com.pubgolf.data.model.RegisterationModels.UserInfoResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferencesManager @Inject constructor(@ApplicationContext val context: Context) {

    private val appContext = context.applicationContext

    private val preferences: SharedPreferences
        get() = appContext.getSharedPreferences(appContext.resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    fun saveVal(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }
    fun getVal(key: String): String? {
        return preferences.getString(key, null)
    }

    fun saveUser(registrationResponse: UserInfoResponse) {
        saveVal("id", registrationResponse.id.toString())
        saveVal("username", registrationResponse.username)
        saveVal("email", registrationResponse.email)
        saveVal("role", registrationResponse.role)
        saveVal("bio", registrationResponse.bio)
        saveVal("photo", registrationResponse.photo)
        saveVal("phone_number", registrationResponse.phone_number)
    }

    fun saveUser(meResponse: MeResponse?) {
        saveVal("id", meResponse?.id.toString())
        saveVal("username", meResponse?.username ?: "Нет данных")
        saveVal("email", meResponse?.email ?: "Нет данных")
        saveVal("role", meResponse?.role ?: "player")
        saveVal("bio", meResponse?.bio ?: "test")
        saveVal("photo", meResponse?.photo ?: "https://sun9-79.userapi.com/impg/HW5jA4PxsJ12WHoEE9J0bo37xh24TDDqG-ehQA/evtKESvh6qE.jpg?size=604x576&quality=96&sign=340bc8b6499869d004150dfe0099c1af&type=album")
        saveVal("phone_number", meResponse?.phone_number ?: "Нет данных")
    }
}

