package com.example.assmobile.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("role") val role: String
)

data class RegisterRequest(
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

/** Matches `students` table — gender: 'male' | 'female' */
data class Student(
    @SerializedName("student_id") val studentId: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName(value = "date_of_birth", alternate = ["dob", "dateOfBirth"]) val dateOfBirth: String? = null,
    @SerializedName(value = "class", alternate = ["class_name", "className"]) val className: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("address") val address: String? = null
)

/** Matches `scores` table */
data class Score(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("student_id") val studentId: String = "",
    @SerializedName("subject") val subject: String = "",
    @SerializedName("score") val score: Double = 0.0,
    @SerializedName("semester") val semester: String? = null,
    @SerializedName("year") val year: String? = null
)

/** Matches `school_info` table */
data class SchoolInfo(
    @SerializedName("school_name") val schoolName: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("principal") val principal: String = ""
)

data class ApiResponse<T>(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null
)
