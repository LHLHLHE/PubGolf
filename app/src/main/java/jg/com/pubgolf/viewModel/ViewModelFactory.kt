package jg.com.pubgolf.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.utils.SharedPreferencesManager
import javax.inject.Inject


class ViewModelFactory @Inject constructor(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(apiHelper, apiHelper.sharedPreferencesManager) as T
        }

        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(apiHelper, apiHelper.sharedPreferencesManager) as T
        }

        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(apiHelper, apiHelper.sharedPreferencesManager) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

