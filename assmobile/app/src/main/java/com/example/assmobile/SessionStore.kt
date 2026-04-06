package com.example.assmobile

/**
 * Holds the last logged-in display name for the home screen (simple session hint).
 * Cleared on logout.
 */
object SessionStore {
    @Volatile
    var displayName: String? = null
}
