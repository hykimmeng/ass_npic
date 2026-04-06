package com.example.assmobile.data.network

/**
 * XAMPP Apache on port **81**: app expects PHP API at `http://<host>:81/school_api/`.
 *
 * - **Android emulator** → use [HOST_FOR_EMULATOR] `10.0.2.2` (maps to your PC’s localhost).
 * - **Physical device** → set [HOST_FOR_EMULATOR] to your PC’s LAN IP (same Wi‑Fi).
 *
 * Deploy: copy the `school_api` folder from this repo to `C:\xampp\htdocs\school_api`
 * (so `http://localhost:81/school_api/api/login.php` works).
 *
 * MySQL in `school_api/config/database.php` uses **port 3307** (import `database/nipc_school.sql`).
 */
object NetworkConfig {
    private const val PORT = 81
    private const val API_ROOT = "school_api"

    const val HOST_FOR_EMULATOR = "10.0.2.2"

    const val BASE_URL = "http://$HOST_FOR_EMULATOR:$PORT/$API_ROOT/"

    const val INDEX_PAGE_URL = "http://$HOST_FOR_EMULATOR:$PORT/$API_ROOT/index.php"
}
