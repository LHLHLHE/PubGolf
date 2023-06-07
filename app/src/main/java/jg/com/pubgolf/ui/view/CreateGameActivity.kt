package jg.com.pubgolf.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.api.RetrofitBuilder
import jg.com.pubgolf.ui.theme.PubGolfTheme
import jg.com.pubgolf.ui.view.navigation.screens.CreateGameScreen
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.GameViewModel
import jg.com.pubgolf.viewModel.UserViewModel
import jg.com.pubgolf.viewModel.ViewModelFactory


class CreateGameActivity: ComponentActivity() {
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private val gameViewModel: GameViewModel by viewModels {
        ViewModelFactory(
            ApiHelper(
                RetrofitBuilder.provideApiService(RetrofitBuilder.provideRetrofit()),
                sharedPreferencesManager
            )
        )
    }

    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory(
            ApiHelper(
                RetrofitBuilder.provideApiService(RetrofitBuilder.provideRetrofit()),
                sharedPreferencesManager
            )
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PubGolfTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    sharedPreferencesManager = SharedPreferencesManager(LocalContext.current)
                    CreateGameScreen(userViewModel, gameViewModel)
                }
            }
        }
    }
}
