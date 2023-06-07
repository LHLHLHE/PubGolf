package jg.com.pubgolf.viewModel.state

import jg.com.pubgolf.data.model.GameModels.NewGameResponse

sealed class NewGameState {
    object Loading : NewGameState()
    data class Success(val newGame: NewGameResponse) : NewGameState()
    data class Error(val message: String) : NewGameState()
}
