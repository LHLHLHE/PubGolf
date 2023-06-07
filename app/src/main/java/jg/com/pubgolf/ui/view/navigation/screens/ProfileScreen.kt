package jg.com.pubgolf.ui.view.navigation.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jg.com.pubgolf.R
import jg.com.pubgolf.ui.theme.Purple500
import jg.com.pubgolf.ui.view.*
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.UserViewModel
import jg.com.pubgolf.viewModel.state.FriendsRequestState


@Composable
fun ProfileScreen(viewModel: UserViewModel) {

    val context = LocalContext.current

    val activity = LocalContext.current as Activity

    val sharedPreferencesManager = SharedPreferencesManager(context)

    val friendsRequestState by viewModel.friendsRequestState.collectAsState(initial = FriendsRequestState.Loading)
    val friendsOutputRequestState by viewModel.friendsOutputRequestState.collectAsState(initial = FriendsRequestState.Loading)

    LaunchedEffect(key1 = Unit) {
        viewModel.getFriendRequests()
        viewModel.getFriendOutputRequests()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple500)
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    //СЮДА ПОДРУБАЕМ ФОТКУ ПОЛЬЗОВАТЕЛЯ
                    //Надо наверное будет сделать фото по дефолту, но мб потом
                    painter = painterResource(id = R.drawable.shrek),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(start = 17.dp)
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .clickable {
                            //СМЕНА ФОТКИ
                        }
                )
                Column(
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        text = "@" + sharedPreferencesManager.getVal("username")!!, //СЮДА ПОДРУБАЕМ НИК ПОЛЬЗОВАТЕЛЯ
                        style = MaterialTheme.typography.h1,
                        color = Color.White,
                        fontSize = 26.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "ID: " + sharedPreferencesManager.getVal("id")!!, //СЮДА ПОДРУБАЕМ ID ПОЛЬЗОВАТЕЛЯ
                        style = MaterialTheme.typography.h1,
                        color = Color.White,
                        fontSize = 26.sp
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(5),
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MenuCard(
                        iconId = R.drawable.baseline_manage_accounts_24,
                        title = "Профиль"
                    ) {
                        //Активити с настройками профиля и более подробной инфой
                        context.startActivity(
                            Intent(
                                context,
                                DetailProfileActivity::class.java
                            )
                        )
                        //activity.finish()
                    }
                    MenuCard(iconId = R.drawable.baseline_people_24, title = "Друзья") {
                        context.startActivity(Intent(context, FriendsActivity::class.java))
                        //Активити с друзьями
                    }
                    //RIP 22.05.2023 КНОПКА С ДОСТИЖЕНИЯМИ

                    var requestTitle = "Мои заявки"
                    if(friendsRequestState is FriendsRequestState.Success) {
                        if(viewModel.friendsRequestList.isNotEmpty())
                            requestTitle += ": " + viewModel.friendsRequestList.size
                     }
                    else {
                        requestTitle = "Мои заявки"
                    }
                    MenuCard(
                        iconId = R.drawable.baseline_person_add_alt_1_24,
                        title = requestTitle
                    ) {
                        context.startActivity(
                            Intent(
                                context,
                                FriendsRequestActivity::class.java
                            )
                        )
                        //Активити "О нас"
                    }

                    MenuCard(iconId = R.drawable.baseline_info_24, title = "О нас") {
                        context.startActivity(Intent(context, AboutUsActivity::class.java))
                        //Активити "О нас"
                    }
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Button(
                            modifier = Modifier
                                .width(200.dp)
                                .height(50.dp),
                            onClick = {
                                sharedPreferencesManager.saveVal("token", "")
                                activity.startActivity(
                                    Intent(
                                        context,
                                        LoginActivity::class.java
                                    )
                                )
                                activity.finish()
                            },
                            colors = ButtonDefaults
                                .buttonColors(
                                    backgroundColor = MaterialTheme.colors.primary
                                ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "ВЫЙТИ",
                                style = MaterialTheme.typography.h1,
                                color = Color.White,
                                fontSize = 28.sp
                            )
                        }
                        Spacer(Modifier.height(100.dp))
                    }

                }
            }
        }
    }
}

@Composable
fun MenuCard(iconId: Int, title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(80.dp)
            .padding(8.dp)
            .background(MaterialTheme.colors.background),
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable
                {
                    onClick()
                }
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    painter = painterResource(id = iconId),
                    contentDescription = "",
                    tint = Purple500
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = title,
                    style = MaterialTheme.typography.h1,
                    color = Purple500,
                    fontSize = 32.sp
                )
            }
        }
    }
}