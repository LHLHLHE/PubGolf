package jg.com.pubgolf.ui.view.navigation.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import jg.com.pubgolf.R
import jg.com.pubgolf.data.model.FriendModels.FriendResponse
import jg.com.pubgolf.ui.theme.Purple500
import jg.com.pubgolf.utils.SharedPreferencesManager

@Composable
fun DetailFriendScreen(friend: FriendResponse) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val sharedPreferencesManager = SharedPreferencesManager(context)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(friend.username) },
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
                        //СЮДА ПОДРУБАЕМ ФОТКУ ДРУГА
                        //Надо наверное будет сделать фото по дефолту, но мб потом
                        painter = painterResource(id = R.drawable.shrek),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
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
                            iconId = R.drawable.baseline_numbers_24,
                            hint = "ID",
                            title = friend.id.toString() //Сюда id
                        )
                        InfoCard(
                            iconId = R.drawable.outline_account_circle_24,
                            hint = "Логин",
                            title = friend.username //Сюда логин
                        )
                        InfoCard(
                            iconId = R.drawable.outline_phone_24,
                            hint = "Номер телефона",
                            title = "friend.phoneNumber" //Сюда номер телефона
                            //TODO
                            //НОМЕР NULL СУКА РАЗРАБ ПОФИКСИ
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}
