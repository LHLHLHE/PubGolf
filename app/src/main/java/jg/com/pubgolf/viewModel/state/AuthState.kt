package jg.com.pubgolf.viewModel.state

import jg.com.pubgolf.data.model.AuthModels.AuthResponse

sealed class AuthState {
    object Loading : AuthState()
    data class Success(val authResponse: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}
