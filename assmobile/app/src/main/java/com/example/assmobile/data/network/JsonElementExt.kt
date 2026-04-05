package com.example.assmobile.data.network

import com.google.gson.JsonElement
import com.google.gson.JsonObject

internal fun JsonElement?.asStringOrEmpty(): String {
    if (this == null || isJsonNull) return ""
    if (!isJsonPrimitive) return ""
    val p = asJsonPrimitive
    return when {
        p.isString -> p.asString
        p.isNumber -> p.asNumber.toString()
        p.isBoolean -> if (p.asBoolean) "true" else "false"
        else -> ""
    }
}

internal fun JsonObject.optString(key: String): String? {
    if (!has(key)) return null
    val e = get(key) ?: return null
    val s = e.asStringOrEmpty()
    return s.ifEmpty { null }
}

internal fun JsonObject.unwrapDataObject(): JsonObject {
    return when {
        has("data") && get("data")?.isJsonObject == true -> getAsJsonObject("data")
        has("school") && get("school")?.isJsonObject == true -> getAsJsonObject("school")
        else -> this
    }
}

internal fun JsonElement?.unwrapArray(): com.google.gson.JsonArray? {
    if (this == null || isJsonNull) return null
    if (isJsonArray) return asJsonArray
    if (isJsonObject) {
        val o = asJsonObject
        when {
            o.has("data") && o.get("data").isJsonArray -> return o.getAsJsonArray("data")
            o.has("students") && o.get("students").isJsonArray -> return o.getAsJsonArray("students")
            o.has("scores") && o.get("scores").isJsonArray -> return o.getAsJsonArray("scores")
        }
    }
    return null
}
