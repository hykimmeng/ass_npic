package com.example.assmobile.data.network

import com.example.assmobile.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/login.php")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/register.php")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("api/students.php")
    suspend fun getStudents(): Response<List<Student>>

    @POST("api/students.php")
    suspend fun registerStudent(@Body student: Student): Response<ApiResponse<String>>

    @GET("api/scores.php")
    suspend fun getScores(@Query("student_id") studentId: String): Response<List<Score>>

    @POST("api/scores.php")
    suspend fun postScore(@Body score: Score): Response<ApiResponse<String>>

    @GET("api/school_info.php")
    suspend fun getSchoolInfo(): Response<SchoolInfo>
}
