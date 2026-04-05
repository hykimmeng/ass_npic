package com.example.assmobile.data.network

/**
 * API base matches your PHP app:
 * - Emulator: [BASE_URL] uses **10.0.2.2** (maps to the host PC’s `localhost`).
 * - Physical device: replace host with your computer’s LAN IP (same port **81** and path **school_api**).
 *
 * Equivalent on the dev machine:
 * - `http://localhost:81/school_api/api/login.php`
 * - `http://localhost:81/school_api/api/students.php`
 * - `http://localhost:81/school_api/api/scores.php?student_id=001`
 * - `http://localhost:81/school_api/api/school_info.php`
 * - Web entry: [INDEX_PAGE_URL] → `http://localhost:81/school_api/index.php`
 */
object NetworkConfig {
    private const val PORT = 81
    private const val API_ROOT = "school_api"

    /** Host reachable from a physical device on the same network to your PC’s localhost. */
    const val HOST_FOR_EMULATOR = "192.168.1.37" // Changed from "10.0.2.2" for physical device

    /**
     * Trailing slash required for Retrofit paths like `api/login.php`.
     * Same resource tree as `http://localhost:81/school_api/`.ស
     */
    const val BASE_URL = "http://$HOST_FOR_EMULATOR:$PORT/$API_ROOT/"

    /** Same as `http://localhost:81/school_api/index.php` on the host (open in browser / WebView). */
    const val INDEX_PAGE_URL = "http://$HOST_FOR_EMULATOR:$PORT/$API_ROOT/index.php"
}
