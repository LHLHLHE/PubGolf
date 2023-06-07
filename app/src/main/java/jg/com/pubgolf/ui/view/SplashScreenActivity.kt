package jg.com.pubgolf.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.api.RetrofitBuilder
import jg.com.pubgolf.ui.theme.PubGolfTheme
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.AuthViewModel
import jg.com.pubgolf.viewModel.ViewModelFactory
import jg.com.pubgolf.viewModel.state.MeState

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

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

                    sharedPreferencesManager = SharedPreferencesManager(LocalContext.current)
                    val viewModel: AuthViewModel by viewModels {
                    ViewModelFactory(
                        ApiHelper(
                            RetrofitBuilder.provideApiService(RetrofitBuilder.provideRetrofit()),
                            sharedPreferencesManager
                        )
                    )
                    }
                    val meState  by viewModel.meState.collectAsState(initial = MeState.Loading)
                    if(sharedPreferencesManager.getVal("token") != null) {
                        LaunchedEffect(key1 = Unit) {
                            viewModel.getMe()
                        }
                        when(meState) {
                            is MeState.Loading -> {
                                Row(horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Text("18+", fontSize = 25.sp)
                                        Text(
                                            "Чрезмерное употребление алкоголя вредит вашему здоровью",
                                            fontSize = 25.sp,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(Modifier.height(20.dp))
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                            is MeState.Success -> {

                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            is MeState.Error -> {
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }
                    }
                    else
                    {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SplashWait() {
    Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()){
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Text("18+", fontSize = 25.sp)
            Text("Чрезмерное употребление алкоголя вредит вашему здоровью",
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
            CircularProgressIndicator()
        }

    }
}