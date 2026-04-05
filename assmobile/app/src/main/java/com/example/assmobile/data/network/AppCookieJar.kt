package com.example.assmobile.data.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.concurrent.ConcurrentHashMap

/**
 * Keeps PHP session cookies (e.g. PHPSESSID) across login → students/scores requests.
 */
class AppCookieJar : CookieJar {
    private val store = ConcurrentHashMap<String, MutableList<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookies.isEmpty()) return
        val bucket = store.getOrPut(url.host) { mutableListOf() }
        synchronized(bucket) {
            for (c in cookies) {
                bucket.removeAll { it.name == c.name && it.domain == c.domain && it.path == c.path }
                bucket.add(c)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val bucket = store[url.host] ?: return emptyList()
        synchronized(bucket) {
            return bucket.filter { it.matches(url) }
        }
    }
}
