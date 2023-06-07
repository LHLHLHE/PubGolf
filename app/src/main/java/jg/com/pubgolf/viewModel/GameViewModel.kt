package jg.com.pubgolf.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.model.GameModels.GameResponse
import jg.com.pubgolf.data.model.GameModels.NewGameRequest
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.state.FriendRequestState
import jg.com.pubgolf.viewModel.state.NewGameState
import jg.com.pubgolf.viewModel.state.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    val apiHelper: ApiHelper,
    val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    //Список всех игр
    private val _allGamesState = MutableStateFlow<GameState>(GameState.Loading)
    val allGamesState: StateFlow<GameState> = _allGamesState
    private var _allGamesList = mutableStateOf<List<GameResponse>>(emptyList())
    val allGamesList: List<GameResponse>
        get() = _allGamesList.value

    private val _newGameState = MutableStateFlow<NewGameState>(NewGameState.Loading)
    val newGameState: StateFlow<NewGameState> = _newGameState

    // получить все игры
    fun getGames() {
        viewModelScope.launch {
            _allGamesState.value = GameState.Loading
            try {
                _allGamesList.value = apiHelper.getAllGames().toMutableList()
                _allGamesState.value = GameState.Success(allGamesList)
            } catch (e: Exception) {
                _allGamesState.value = GameState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }

    fun createNewGame(request: NewGameRequest) {
        viewModelScope.launch {
            _newGameState.value = NewGameState.Loading
            try {
                val newGameRequest = apiHelper.createGame(request)
                Log.d("penis2", newGameRequest.toString())
                Log.d("penis2", request.toString())
                Log.d("penis2", newGameState.toString())
                _newGameState.value = NewGameState.Success(newGameRequest)
            } catch (e: Exception) {
                _newGameState.value = NewGameState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }
}