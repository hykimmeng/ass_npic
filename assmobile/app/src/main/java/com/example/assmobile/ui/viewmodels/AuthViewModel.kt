package com.example.assmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assmobile.data.model.LoginRequest
import com.example.assmobile.data.model.LoginResponse
import com.example.assmobile.data.model.RegisterRequest
import com.example.assmobile.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun login(username: String, password: String, role: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = repository.login(LoginRequest(username, password, role))
                if (response.isSuccessful && response.body()?.status == "success") {
                    _loginState.value = LoginState.Success(response.body()!!)
                } else {
                    _loginState.value = LoginState.Error(response.body()?.message ?: "Login failed")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }

    fun register(username: String, password: String, role: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val response = repository.register(RegisterRequest(username, password, role))
                if (response.isSuccessful && response.body()?.status == "success") {
                    _registerState.value = RegisterState.Success
                } else {
                    _registerState.value = RegisterState.Error(response.body()?.message ?: "Registration failed")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }

    fun resetRegisterState() {
        _registerState.value = RegisterState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val response: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}
