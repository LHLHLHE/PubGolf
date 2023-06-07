package jg.com.pubgolf.ui.view.navigation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
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
import jg.com.pubgolf.data.model.FriendModels.FriendResponse
import jg.com.pubgolf.data.model.GameModels.NewGameRequest
import jg.com.pubgolf.ui.theme.Purple500
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.GameViewModel
import jg.com.pubgolf.viewModel.UserViewModel
import jg.com.pubgolf.viewModel.state.FriendState
import jg.com.pubgolf.viewModel.state.NewGameState
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateGameScreen(userViewModel: UserViewModel, gameViewModel: GameViewModel) {
    //TODO Обработка ошибок, если надо, я просто хз как
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val sharedPreferencesManager = SharedPreferencesManager(context)

    val friendState by userViewModel.friendState.collectAsState(initial = FriendState.Loading)
    val newGameState by gameViewModel.newGameState.collectAsState(initial = NewGameState.Loading)

    LaunchedEffect(friendState, newGameState) {
        when (friendState) {
            is FriendState.Loading -> {
                userViewModel.getFriends()
            }
            is FriendState.Success -> {

            }
            is FriendState.Error -> {
                userViewModel.getFriends()
                Toast.makeText(context, "Ошибка получения игр!", Toast.LENGTH_LONG).show()
            }
        }
        when (newGameState) {
            is NewGameState.Loading -> {

            }
            is NewGameState.Success -> {
                //TODO Закидывать в игру
                activity.finish()
                Toast.makeText(context, "Игра создана", Toast.LENGTH_LONG).show()
            }
            is NewGameState.Error -> {
                Toast.makeText(context, "Ошибка создания игры!", Toast.LENGTH_LONG).show()
            }
        }
    }

    val gameName = remember { mutableStateOf("") }
    val difficult = remember { mutableStateOf("") }
    val budget = remember { mutableStateOf("") }
    val selectedFriendList = remember { mutableListOf<Int>() }

    val selectedDifficult = mapOf(
        "Подпивасник" to "underbeerman",
        "Любитель" to "fan",
        "Фриланголик" to "freelanholic"
    )

    val selectedBudget = mapOf(
        "Бомж" to "homeless",
        "Любитель" to "fan",
        "Мажор" to "major"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Создание игры") },
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
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = gameName.value,
                onValueChange = { login -> gameName.value = login },
                placeholder = { Text("Имя комнаты") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary, // Цвет границы при фокусе
                    unfocusedBorderColor = MaterialTheme.colors.primaryVariant, // Цвет границы при отсутствии фокуса
                    errorBorderColor = Color.Red // Цвет границы при ошибке
                )
            )
            DropMenu("Сложность", difficult, listOf("Подпивасник", "Любитель", "Фриланголик"))
            DropMenu("Бюджет", budget, listOf("Бомж", "Любитель", "Мажор"))
            LazyColumn(
                modifier = Modifier
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0x332F3273))
                    .height(250.dp)
                    .width(280.dp)
            ) {
                items(userViewModel.friendList.size) { item ->
                    SmallFriendCard(userViewModel.friendList[item], selectedFriendList)
                }
            }
            Button(
                onClick = {
                    selectedFriendList.add(sharedPreferencesManager.getVal("id")!!.toInt())
                    gameViewModel.createNewGame(
                        NewGameRequest(
                            selectedFriendList,
                            gameName.value,
                            selectedDifficult[difficult.value].toString(),
                            selectedBudget[budget.value].toString(),
                            "created",
                            getCurrentDateTime()
                        )
                    )
                }) {
                Text(text = "Создать")
            }
        }
    }
}

@Composable
fun DropMenu(hint: String, selectedText: MutableState<String>, list: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(
        modifier = Modifier
            .padding(0.dp)
            .width(280.dp)
    )
    {
        OutlinedTextField(
            value = selectedText.value,
            onValueChange = { selectedText.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = hint) },
            trailingIcon = {
                Icon(
                    icon,
                    "contentDescription",
                    Modifier.clickable { expanded = !expanded }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
        ) {
            list.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText.value = label
                    expanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
}

@Composable
fun SmallFriendCard(friend: FriendResponse, selectedFriend: MutableList<Int>) {
    val iconState = remember { mutableStateOf(R.drawable.baseline_add_24) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(5.dp)),
                painter = painterResource(id = R.drawable.shrek),
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
            Text(
                text = friend.username,
                style = MaterialTheme.typography.h1,
                color = Purple500,
                fontSize = 16.sp
            )
            Icon(
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        selectedFriend.add(friend.id)
                        Log.d("penis", selectedFriend.toString())
                        iconState.value = R.drawable.baseline_done_24
                    },
                painter = painterResource(id = iconState.value),
                contentDescription = "",
                tint = Purple500
            )
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val currentTime = Date()
    return dateFormat.format(currentTime)
}