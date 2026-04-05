package com.example.assmobile.data.network

import com.example.assmobile.data.model.ApiResponse
import com.example.assmobile.data.model.LoginResponse
import org.json.JSONObject
import retrofit2.Response

fun LoginResponse.isLoginSuccess(): Boolean {
    val s = status.trim().lowercase()
    return s == "success" || s == "ok" || s == "1" || s == "true"
}

fun <T> ApiResponse<T>.isApiSuccess(): Boolean {
    val s = status.trim().lowercase()
    return s == "success" || s == "ok" || s == "1" || s == "true"
}

fun <T> Response<T>.httpErrorDetail(fallback: String): String {
    val err = errorBody()?.use { it.string() } ?: return message().ifEmpty { fallback }
    return try {
        JSONObject(err).optString("message").ifEmpty { err }
    } catch (_: Exception) {
        err.ifEmpty { message().ifEmpty { fallback } }
    }
}
