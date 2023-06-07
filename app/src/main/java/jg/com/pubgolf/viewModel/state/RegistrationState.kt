package jg.com.pubgolf.viewModel.state

import jg.com.pubgolf.data.model.RegisterationModels.UserInfoResponse

sealed class RegistrationState {
    object Loading : RegistrationState()
    data class Success(val userInfoResponse: UserInfoResponse) : RegistrationState()
    data class Error(val response: String) : RegistrationState()
}