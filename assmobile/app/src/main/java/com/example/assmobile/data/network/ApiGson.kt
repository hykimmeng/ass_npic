package com.example.assmobile.data.network

import com.example.assmobile.data.model.ApiResponse
import com.example.assmobile.data.model.LoginResponse
import com.example.assmobile.data.model.SchoolInfo
import com.example.assmobile.data.model.Score
import com.example.assmobile.data.model.Student
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private class LoginResponseDeserializer : JsonDeserializer<LoginResponse> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LoginResponse {
        val root = json.asJsonObject
        val source = if (root.has("user") && root.get("user").isJsonObject) {
            root.getAsJsonObject("user")
        } else {
            root
        }
        val statusRaw = root.get("status").asStringOrEmpty()
            .ifEmpty { root.get("success").asStringOrEmpty() }
        val normalizedStatus = normalizeStatus(statusRaw, root.get("success"))
        val message = root.optString("message") ?: ""
        val userId = source.optString("user_id") ?: source.optString("id") ?: root.optString("user_id")
        val username = source.optString("username") ?: root.optString("username")
        val role = source.optString("role") ?: root.optString("role")
        return LoginResponse(
            status = normalizedStatus,
            message = message,
            userId = userId,
            username = username,
            role = role
        )
    }

    private fun normalizeStatus(statusStr: String, successElem: JsonElement?): String {
        var s = statusStr.trim().lowercase()
        if (s == "1" || s == "true" || s == "ok") s = "success"
        if (s.isNotEmpty()) return s
        if (successElem != null && successElem.isJsonPrimitive) {
            val p = successElem.asJsonPrimitive
            if (p.isBoolean && p.asBoolean) return "success"
            if (p.isNumber && p.asNumber.toInt() == 1) return "success"
        }
        return ""
    }
}

private class StudentListDeserializer : JsonDeserializer<List<Student>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<Student> {
        val arr = json.unwrapArray() ?: return emptyList()
        val list = mutableListOf<Student>()
        for (el in arr) {
            runCatching { context.deserialize(el, Student::class.java) as Student }.onSuccess { list.add(it) }
        }
        return list
    }
}

private class ScoreListDeserializer : JsonDeserializer<List<Score>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<Score> {
        val arr = json.unwrapArray() ?: return emptyList()
        val list = mutableListOf<Score>()
        for (el in arr) {
            runCatching { context.deserialize(el, Score::class.java) as Score }.onSuccess { list.add(it) }
        }
        return list
    }
}

private class SchoolInfoDeserializer : JsonDeserializer<SchoolInfo> {
    private val plainGson = GsonBuilder().create()

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SchoolInfo {
        val obj = json.asJsonObject.unwrapDataObject()
        return plainGson.fromJson(obj, SchoolInfo::class.java)
    }
}

private class ApiResponseStringDeserializer : JsonDeserializer<ApiResponse<String>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ApiResponse<String> {
        val obj = json.asJsonObject
        val status = obj.get("status").asStringOrEmpty()
            .ifEmpty { obj.get("success").asStringOrEmpty() }
            .ifEmpty {
                val s = obj.get("success")
                if (s != null && s.isJsonPrimitive && s.asJsonPrimitive.isBoolean) {
                    if (s.asBoolean) "success" else "error"
                } else ""
            }
        val message = obj.optString("message") ?: ""
        val dataEl = obj.get("data")
        val dataStr = when {
            dataEl == null || dataEl.isJsonNull -> null
            dataEl.isJsonPrimitive -> dataEl.asStringOrEmpty().ifEmpty { null }
            else -> dataEl.toString()
        }
        return ApiResponse(
            status = status.ifEmpty { "unknown" },
            message = message,
            data = dataStr
        )
    }
}

fun buildApiGson(): Gson {
    val studentListType = object : TypeToken<List<Student>>() {}.type
    val scoreListType = object : TypeToken<List<Score>>() {}.type
    val apiStringType = object : TypeToken<ApiResponse<String>>() {}.type
    return GsonBuilder()
        .registerTypeAdapter(LoginResponse::class.java, LoginResponseDeserializer())
        .registerTypeAdapter(studentListType, StudentListDeserializer())
        .registerTypeAdapter(scoreListType, ScoreListDeserializer())
        .registerTypeAdapter(SchoolInfo::class.java, SchoolInfoDeserializer())
        .registerTypeAdapter(apiStringType, ApiResponseStringDeserializer())
        .setLenient()
        .create()
}
