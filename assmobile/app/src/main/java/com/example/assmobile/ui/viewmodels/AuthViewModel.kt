package com.example.assmobile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assmobile.data.model.LoginRequest
import com.example.assmobile.data.model.LoginResponse
import com.example.assmobile.data.model.RegisterRequest
import com.example.assmobile.data.network.httpErrorDetail
import com.example.assmobile.data.network.isLoginSuccess
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

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun login(username: String, password: String, role: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = repository.login(
                    LoginRequest(
                        username = username.trim(),
                        password = password.trim(),
                        role = role
                    )
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    when {
                        body != null && body.isLoginSuccess() -> {
                            _loginState.value = LoginState.Success(body)
                        }
                        body != null -> {
                            val msg = body.message.ifBlank { "Login failed" }
                            _loginState.value = LoginState.Error(msg)
                        }
                        else -> {
                            _loginState.value = LoginState.Error("Empty response from server")
                        }
                    }
                } else {
                    _loginState.value = LoginState.Error(response.httpErrorDetail("Login failed"))
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(
                    e.localizedMessage ?: "Cannot reach server. Use emulator (10.0.2.2) or your PC IP on a real device."
                )
            }
        }
    }

    fun register(username: String, password: String, role: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val response = repository.register(
                    RegisterRequest(username.trim(), password.trim(), role)
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    when {
                        body == null -> _registerState.value = RegisterState.Success
                        body.isLoginSuccess() -> _registerState.value = RegisterState.Success
                        else -> {
                            _registerState.value = RegisterState.Error(
                                body.message.ifBlank { "Registration failed" }
                            )
                        }
                    }
                } else {
                    _registerState.value = RegisterState.Error(response.httpErrorDetail("Registration failed"))
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
