package jg.com.pubgolf.ui.view

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jg.com.pubgolf.ui.theme.PubGolfTheme
import jg.com.pubgolf.ui.view.navigation.screens.AboutUsScreen

class AboutUsActivity : ComponentActivity() {

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
                    AboutUsScreen()
                }
            }
        }
    }
}