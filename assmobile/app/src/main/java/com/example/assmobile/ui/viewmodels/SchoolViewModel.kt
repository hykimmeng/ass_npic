package com.example.assmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assmobile.data.model.*
import com.example.assmobile.data.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SchoolViewModel : ViewModel() {
    private val studentRepo = StudentRepository()
    private val scoreRepo = ScoreRepository()
    private val schoolRepo = SchoolRepository()

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    private val _schoolInfo = MutableStateFlow<SchoolInfo?>(null)
    val schoolInfo: StateFlow<SchoolInfo?> = _schoolInfo

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadStudents()
        loadSchoolInfo()
    }

    private fun loadStudents() {
        viewModelScope.launch {
            try {
                val response = studentRepo.getStudents()
                if (response.isSuccessful) {
                    _students.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun loadSchoolInfo() {
        viewModelScope.launch {
            try {
                val response = schoolRepo.getSchoolInfo()
                if (response.isSuccessful) {
                    _schoolInfo.value = response.body()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun registerStudent(student: Student) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = studentRepo.register(student)
                if (response.isSuccessful) {
                    _uiState.value = UiState.Success("Student registered successfully")
                    loadStudents()
                } else {
                    _uiState.value = UiState.Error(response.body()?.message ?: "Failed to register")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun enterScore(score: Score) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = scoreRepo.postScore(score)
                if (response.isSuccessful) {
                    _uiState.value = UiState.Success("Score saved successfully")
                } else {
                    _uiState.value = UiState.Error(response.body()?.message ?: "Failed to save score")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}
