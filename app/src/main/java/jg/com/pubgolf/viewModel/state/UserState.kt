package jg.com.pubgolf.viewModel.state

import jg.com.pubgolf.data.model.FriendModels.FriendResponse


sealed class UserState {
    object Loading : UserState()
    data class Success(val users: List<FriendResponse>) : UserState()
    data class Error(val response: String) : UserState()
}