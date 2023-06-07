package jg.com.pubgolf.viewModel.state

import jg.com.pubgolf.data.model.FriendModels.FriendRequestResponse
import jg.com.pubgolf.data.model.FriendModels.FriendResponse

sealed class FriendsRequestState {
    object Loading : FriendsRequestState()
    data class Success(val firendList: List<FriendRequestResponse>) : FriendsRequestState()
    data class Error(val message: String) : FriendsRequestState()
}

sealed class FriendRequestState {
    object Loading : FriendRequestState()
    data class Success(val firend: FriendRequestResponse) : FriendRequestState()
    data class Error(val message: String) : FriendRequestState()
}

