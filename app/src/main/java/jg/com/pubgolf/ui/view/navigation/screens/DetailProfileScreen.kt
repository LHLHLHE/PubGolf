package jg.com.pubgolf.ui.view.navigation.screens

import jg.com.pubgolf.R
import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jg.com.pubgolf.ui.theme.Purple500
import jg.com.pubgolf.ui.theme.Purple700
import jg.com.pubgolf.utils.SharedPreferencesManager

@Composable
fun DetailProfileScreen() {

    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val sharedPreferencesManager = SharedPreferencesManager(context)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = {
                        activity.finish()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                })
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Purple500)) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary)
                        .height(165.dp)
                        .padding(15.dp)
                        .clip(RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp)),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        //СЮДА ПОДРУБАЕМ ФОТКУ ПОЛЬЗОВАТЕЛЯ
                        //Надо наверное будет сделать фото по дефолту, но мб потом
                        painter = painterResource(id = R.drawable.shrek),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .clickable {
                                //СМЕНА ФОТКИ
                            }
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = it)
                            .background(MaterialTheme.colors.background),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        InfoCard(
                            iconId = R.drawable.outline_account_circle_24,
                            hint = "Логин",
                            title = sharedPreferencesManager.getVal("username")!! //Сюда логин
                        )
                        InfoCard(
                            iconId = R.drawable.baseline_alternate_email_24,
                            hint = "Эл. почта",
                            title = sharedPreferencesManager.getVal("email")!! //Сюда почту
                        )
                        InfoCard(
                            iconId = R.drawable.outline_phone_24,
                            hint = "Номер телефона",
                            title = sharedPreferencesManager.getVal("phone_number")!! //Сюда номер телефона
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Button(
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(50.dp),
                                onClick = {
                                    //Изменить инфу об аккаунте
                                },
                                colors = ButtonDefaults
                                    .buttonColors(
                                        backgroundColor = MaterialTheme.colors.primary
                                    ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "ИЗМЕНИТЬ",
                                    style = MaterialTheme.typography.h1,
                                    color = Color.White,
                                    fontSize = 28.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }
        }
    }

}



@Composable
fun InfoCard(iconId: Int, hint: String, title: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(85.dp)
            .padding(10.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .size(60.dp),
                shape = RoundedCornerShape(90.dp),
                border = BorderStroke(0.dp, Color.Transparent),
                elevation = 0.dp
            ) {
                Image(
                    modifier = Modifier.padding(5.dp),
                    painter = painterResource(id = iconId),
                    contentDescription = ""
                )
            }

            Column(

            ) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.h1,
                    fontSize = 16.sp,
                    color = Purple700
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.h1,
                    fontSize = 26.sp,
                    color = Purple500
                )
                Divider(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    color = Purple500,
                    thickness = 1.dp
                )
            }
        }
    }
}