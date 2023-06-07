package jg.com.pubgolf.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.model.AuthModels.AuthRequest
import jg.com.pubgolf.data.model.FriendModels.FriendRequestResponse
import jg.com.pubgolf.data.model.FriendModels.FriendResponse
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.state.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val apiHelper: ApiHelper, val sharedPreferencesManager: SharedPreferencesManager) : ViewModel()  {

    // принятие в друзья
    private val _friendRequestAccept = MutableStateFlow<FriendRequestState>(FriendRequestState.Loading)
    val friendRequestAccept: StateFlow<FriendRequestState> = _friendRequestAccept

    // список друзей
    private val _friendState = MutableStateFlow<FriendState>(FriendState.Loading)
    val friendState: StateFlow<FriendState> = _friendState
    private var _friendList = mutableStateOf<List<FriendResponse>>(emptyList())
    val friendList: List<FriendResponse>
        get() = _friendList.value

    // список входящих запросов
    private val _friendsRequestState = MutableStateFlow<FriendsRequestState>(FriendsRequestState.Loading)
    val friendsRequestState: StateFlow<FriendsRequestState> = _friendsRequestState
    private var _friendsRequestList = mutableStateOf<List<FriendRequestResponse>>(emptyList())
    val friendsRequestList: List<FriendRequestResponse>
        get() = _friendsRequestList.value

    // список исходящих запросов
    private val _friendsOutputRequestState = MutableStateFlow<FriendsRequestState>(FriendsRequestState.Loading)
    val friendsOutputRequestState: StateFlow<FriendsRequestState> = _friendsOutputRequestState
    private var _friendsOutputRequestList = mutableStateOf<List<FriendRequestResponse>>(emptyList())
    val friendsOutputRequestList: List<FriendRequestResponse>
        get() = _friendsOutputRequestList.value

    // отправить запрос
    private val _friendRequestState = MutableStateFlow<FriendRequestState>(FriendRequestState.Loading)
    val friendRequestState: StateFlow<FriendRequestState> = _friendRequestState

    // список всех пользователей
    private val _allUsersState = MutableStateFlow<UserState>(UserState.Loading)
    val allUsersState: StateFlow<UserState> = _allUsersState
    private var _allUsersList = mutableStateOf<List<FriendResponse>>(emptyList())
    val allUsersList: List<FriendResponse>
        get() = _allUsersList.value



    // получить всех друзей
    fun getFriends() {
        viewModelScope.launch {
            _friendState.value = FriendState.Loading
            try {
                _friendList.value = apiHelper.getFriends().toMutableList()
                _friendState.value = FriendState.Success(friendList)
            } catch (e: Exception) {
                _friendState.value = FriendState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }

    // получить запросы в друзья (входящие)
    fun getFriendRequests() {
        viewModelScope.launch {
            _friendsRequestState.value = FriendsRequestState.Loading
            try {
                _friendsRequestList.value = apiHelper.getFriendsRequest().toMutableList()
                _friendsRequestState.value = FriendsRequestState.Success(friendsRequestList)
            } catch (e: Exception) {
                _friendsRequestState.value = FriendsRequestState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }

    // получить запросы в друзья (исходящие)
    fun getFriendOutputRequests() {
        viewModelScope.launch {
            _friendsOutputRequestState.value = FriendsRequestState.Loading
            try {
                _friendsOutputRequestList.value = apiHelper.getFriendsOutputRequest().toMutableList()
                _friendsOutputRequestState.value = FriendsRequestState.Success(friendsOutputRequestList)
            } catch (e: Exception) {
                _friendsOutputRequestState.value = FriendsRequestState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }


    // получить всех пользователей
    fun getUsers() {
        viewModelScope.launch {
            _allUsersState.value = UserState.Loading
            try {
                _allUsersList.value = apiHelper.getAllUsers().toMutableList()
                _allUsersState.value = UserState.Success(allUsersList)
            } catch (e: Exception) {
                _allUsersState.value = UserState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }


    // отправить запрос в друзья
    fun sendFriendRequest(userId: Int) {
        viewModelScope.launch {
            try {
                val friendRequest = apiHelper.sendFriendRequest(userId)
                _friendRequestState.value = FriendRequestState.Success(friendRequest)
            } catch (e: Exception) {
                _friendRequestState.value = FriendRequestState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }

    // принять запрос
    fun acceptFriendRequest(userId: Int) {
        viewModelScope.launch {
            try {
                val friendRequestResponse = apiHelper.acceptFriendRequest(userId)
                _friendRequestState.value = FriendRequestState.Success(friendRequestResponse)
            } catch (e: Exception) {
                _friendRequestState.value = FriendRequestState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }



    // удалить запрос
    fun deleteFriend(userId: Int) {
        viewModelScope.launch {
            try {
                apiHelper.deleteFriend(userId)
            } catch (e: Exception) {
                println(e)
            }
        }
    }


}
