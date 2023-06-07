package jg.com.pubgolf.viewModel.state

import jg.com.pubgolf.data.model.GameModels.GameResponse

sealed class GameState {
    object Loading : GameState()
    data class Success(val games: List<GameResponse>) : GameState()
    data class Error(val message: String) : GameState()
}
