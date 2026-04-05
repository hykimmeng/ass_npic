package com.example.assmobile.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("role") val role: String
)

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("role") val role: String? = null
)

data class Student(
    @SerializedName("student_id") val studentId: String,
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("dob") val dob: String,
    @SerializedName("class_name") val className: String
)

data class Score(
    @SerializedName("id") val id: String? = null,
    @SerializedName("student_id") val studentId: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("score") val score: Double,
    @SerializedName("date") val date: String? = null
)

data class SchoolInfo(
    @SerializedName("school_name") val schoolName: String,
    @SerializedName("address") val address: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("website") val website: String
)

data class ApiResponse<T>(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null
)
