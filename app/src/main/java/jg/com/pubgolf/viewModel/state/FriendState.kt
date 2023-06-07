package jg.com.pubgolf.viewModel.state


import jg.com.pubgolf.data.model.FriendModels.FriendResponse

sealed class FriendState {
    object Loading : FriendState()
    data class Success(val friendList: List<FriendResponse>) : FriendState()
    data class Error(val message: String) : FriendState()
}
