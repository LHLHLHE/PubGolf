package jg.com.pubgolf.ui.view

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.api.RetrofitBuilder
import jg.com.pubgolf.ui.theme.PubGolfTheme
import jg.com.pubgolf.ui.view.navigation.screens.FriendsScreen
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.UserViewModel
import jg.com.pubgolf.viewModel.ViewModelFactory
import jg.com.pubgolf.viewModel.state.FriendState
import jg.com.pubgolf.viewModel.state.FriendsRequestState

class FriendsActivity : ComponentActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    // viewModel пользователя
    private val viewModel: UserViewModel by viewModels {
        ViewModelFactory(
            ApiHelper(
                RetrofitBuilder.provideApiService(RetrofitBuilder.provideRetrofit()),
                sharedPreferencesManager
            )
        )
    }

    var loading: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)
        setContent {
            PubGolfTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    val systemUiController: SystemUiController = rememberSystemUiController()
                    systemUiController.isSystemBarsVisible = false
                    val context = LocalContext.current
                    sharedPreferencesManager = SharedPreferencesManager(context)
                    // состояние ответа в вьюмоделе
                    val friendState by viewModel.friendState.collectAsState(initial = FriendState.Loading)
                    val requestState by viewModel.friendsRequestState.collectAsState(initial = FriendsRequestState.Loading)

                    LaunchedEffect(friendState) {
                        viewModel.getFriends()
                        if (friendState is FriendState.Success) {
                            loading = false
                        }
                        if (friendState is FriendState.Error) {
                            Toast.makeText(context, "Ошибка в получении друзей", Toast.LENGTH_LONG).show()
                        }
                        viewModel.getFriendRequests()
                    }
                    FriendsScreen(loading, viewModel)
                }
            }
        }
    }
}