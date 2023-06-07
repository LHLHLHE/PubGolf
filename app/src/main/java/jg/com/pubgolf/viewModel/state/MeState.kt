package jg.com.pubgolf.viewModel.state

import jg.com.pubgolf.data.model.MeResponse
import jg.com.pubgolf.data.model.MeResponseList

sealed class MeState {
    object Loading : MeState()
    data class Success(val me: MeResponse) : MeState()
    data class Error(val message: String) : MeState()
}
