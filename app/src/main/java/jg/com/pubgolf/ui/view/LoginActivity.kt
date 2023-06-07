package jg.com.pubgolf.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import jg.com.pubgolf.R
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.api.RetrofitBuilder
import jg.com.pubgolf.data.model.AuthModels.AuthRequest
import jg.com.pubgolf.ui.theme.PubGolfTheme
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.AuthViewModel
import jg.com.pubgolf.viewModel.ViewModelFactory
import jg.com.pubgolf.viewModel.state.AuthState

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    // viewModel авторизации
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(
            ApiHelper(
                RetrofitBuilder.provideApiService(RetrofitBuilder.provideRetrofit()),
                sharedPreferencesManager
            )
        )
    }


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
                    LoginScreen(viewModel, sharedPreferencesManager)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: AuthViewModel, sharedPreferencesManager: SharedPreferencesManager) {

    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false // Status bar

    // хранение логина
    val loginText = remember{mutableStateOf("")}

    // хранение пароля
    val passwordText = remember{mutableStateOf("")}

    // хранение контекста
    val context = LocalContext.current

    val activity = LocalContext.current as Activity

    // состояние ответа в вьюмоделе
    val state by viewModel.authState.collectAsState(initial = AuthState.Loading)



    // состояние пароля
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // Запуск активити, если пришел норм результат
    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            viewModel.getMe()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
        if (state is AuthState.Error) {
            Toast.makeText(context, "Проверьте логин и пароль", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Image(
                modifier = Modifier.size(120.dp),
                painter = painterResource(id = R.drawable.ic_cheers),
                contentDescription = "",
                alignment = Alignment.TopCenter
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Pub",
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.h1
            )
            Text(
                "Golf",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h1
            )
        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Не потребляй, а дегустируй!",
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.h4,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(45.dp))

        Spacer(modifier = Modifier.height(5.dp))
        // Поле ввода логина
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = loginText.value,
                onValueChange = { login -> loginText.value = login },
                placeholder = {Text("Введите логин")},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary, // Цвет границы при фокусе
                    unfocusedBorderColor = MaterialTheme.colors.primaryVariant, // Цвет границы при отсутствии фокуса
                    errorBorderColor = Color.Red // Цвет границы при ошибке
                )
            )
        }
        Spacer(modifier = Modifier.height(5.dp))

        // Поле ввода пароля
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = passwordText.value,
                onValueChange = { password -> passwordText.value = password },
                singleLine = true,
                placeholder = {Text("Введите пароль")},
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff


                    val description = if (passwordVisible) "Скрыть пароль" else "показать пароль"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary, // Цвет границы при фокусе
                    unfocusedBorderColor = MaterialTheme.colors.primaryVariant, // Цвет границы при отсутствии фокуса
                    errorBorderColor = Color.Red // Цвет границы при ошибке
                )
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        // Кнопка входа
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier
                    .width(278.dp)
                    .height(50.dp),
                onClick = {
                if (loginText.value != "" && passwordText.value != "") {
                    val authRequest = AuthRequest(loginText.value, passwordText.value)
                    viewModel.authenticateUser(authRequest)
                    }
                },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults
                    .buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    )
            ) {
                Text(
                    "Войти",
                    color = Color.White,
                    style = MaterialTheme.typography.h1,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Кнопка регистрации
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier
                    .width(278.dp)
                    .height(50.dp),
                onClick = {
                    activity.startActivity(Intent(context, RegistrationActivity::class.java))
                },
                colors = ButtonDefaults
                    .buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    ),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    "Зарегистрироваться",
                    color = Color.White,
                    style = MaterialTheme.typography.h1,
                    fontSize = 16.sp,
                )
            }
        }
    }
}





