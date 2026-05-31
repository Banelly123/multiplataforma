package com.example.multiplataforma.auth

import android.content.Context
import android.content.SharedPreferences

/**
 * AndroidSessionManager - Guardado solo como respaldo para Android
 */
class AndroidSessionManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "aliadas_prefs"
        private const val KEY_EMAIL  = "user_email"
        private const val KEY_LOGGED = "is_logged_in"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun guardarSesion(email: String) {
        prefs.edit()
            .putString(KEY_EMAIL, email)
            .putBoolean(KEY_LOGGED, true)
            .apply()
    }

    fun cerrarSesion() {
        prefs.edit()
            .clear()
            .apply()
    }

    fun haySessionActiva(): Boolean {
        return prefs.getBoolean(KEY_LOGGED, false)
    }

    fun obtenerEmail(): String? {
        return prefs.getString(KEY_EMAIL, null)
    }
}
