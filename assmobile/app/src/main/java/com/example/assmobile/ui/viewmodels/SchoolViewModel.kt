package com.example.assmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assmobile.data.model.SchoolInfo
import com.example.assmobile.data.model.Score
import com.example.assmobile.data.model.Student
import com.example.assmobile.data.network.httpErrorDetail
import com.example.assmobile.data.network.isApiSuccess
import com.example.assmobile.data.repository.ScoreRepository
import com.example.assmobile.data.repository.SchoolRepository
import com.example.assmobile.data.repository.StudentRepository
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

    fun loadStudents() {
        viewModelScope.launch {
            try {
                val response = studentRepo.getStudents()
                if (response.isSuccessful) {
                    _students.value = response.body() ?: emptyList()
                }
            } catch (_: Exception) {
                // Keep list empty; user can retry after login/session
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
            } catch (_: Exception) {
            }
        }
    }

    fun registerStudent(student: Student) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = studentRepo.register(student)
                if (response.isSuccessful) {
                    val body = response.body()
                    val ok = body == null || body.isApiSuccess()
                    if (ok) {
                        _uiState.value = UiState.Success(
                            body?.message?.ifBlank { null } ?: "Student registered successfully"
                        )
                        loadStudents()
                    } else {
                        _uiState.value = UiState.Error(
                            body?.message?.takeIf { it.isNotBlank() } ?: "Failed to register student"
                        )
                    }
                } else {
                    _uiState.value = UiState.Error(response.httpErrorDetail("Failed to register student"))
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
                    val body = response.body()
                    val ok = body == null || body.isApiSuccess()
                    if (ok) {
                        _uiState.value = UiState.Success(
                            body?.message?.ifBlank { null } ?: "Score saved successfully"
                        )
                    } else {
                        _uiState.value = UiState.Error(
                            body?.message?.takeIf { it.isNotBlank() } ?: "Failed to save score"
                        )
                    }
                } else {
                    _uiState.value = UiState.Error(response.httpErrorDetail("Failed to save score"))
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun clearMessages() {
        _uiState.value = UiState.Idle
    }
}

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}
