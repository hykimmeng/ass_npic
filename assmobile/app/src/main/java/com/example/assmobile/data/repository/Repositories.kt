package com.example.assmobile.data.repository

import com.example.assmobile.data.model.*
import com.example.assmobile.data.network.RetrofitClient
import retrofit2.Response

class AuthRepository {
    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return RetrofitClient.instance.login(request)
    }
}

class StudentRepository {
    suspend fun getStudents(): Response<List<Student>> {
        return RetrofitClient.instance.getStudents()
    }

    suspend fun register(student: Student): Response<ApiResponse<String>> {
        return RetrofitClient.instance.registerStudent(student)
    }
}

class ScoreRepository {
    suspend fun getScores(studentId: String): Response<List<Score>> {
        return RetrofitClient.instance.getScores(studentId)
    }

    suspend fun postScore(score: Score): Response<ApiResponse<String>> {
        return RetrofitClient.instance.postScore(score)
    }
}

class SchoolRepository {
    suspend fun getSchoolInfo(): Response<SchoolInfo> {
        return RetrofitClient.instance.getSchoolInfo()
    }
}
