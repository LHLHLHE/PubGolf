package jg.com.pubgolf.ui.view.navigation.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
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
import androidx.compose.ui.window.Dialog
import com.google.gson.Gson
import jg.com.pubgolf.R
import jg.com.pubgolf.data.model.FriendModels.FriendRequestResponse
import jg.com.pubgolf.data.model.FriendModels.FriendResponse
import jg.com.pubgolf.ui.theme.Green500
import jg.com.pubgolf.ui.theme.Purple500
import jg.com.pubgolf.ui.theme.Red500
import jg.com.pubgolf.ui.view.DetailFriendActivity
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.UserViewModel
import jg.com.pubgolf.viewModel.state.FriendState
import jg.com.pubgolf.viewModel.state.FriendsRequestState
import jg.com.pubgolf.viewModel.state.UserState


@Composable
fun FriendsScreen(loading: Boolean, viewModel: UserViewModel) {

    val activity = LocalContext.current as Activity


    val friendState by viewModel.friendState.collectAsState(initial = FriendState.Loading)
    val requestState by viewModel.friendsRequestState.collectAsState(initial = FriendsRequestState.Loading)


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        topBar = {
            TopAppBar(
                title = { Text("Друзья") },
                navigationIcon = {
                    IconButton(onClick = {
                        activity.finish()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                })
        }
    ) {
        val sharedPreferencesManager = SharedPreferencesManager(LocalContext.current)
        val openDialog = remember { mutableStateOf(false) }
        Spacer(Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Purple500),
            contentAlignment = Alignment.Center
        ) {
            Column() {
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.height(40.dp),
                        onClick = {
                            viewModel.getUsers()
                            viewModel.getFriendRequests()
                            viewModel.getFriendOutputRequests()
                            openDialog.value = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Green500,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Найти друга")
                        Icon(Icons.Default.ArrowRight, "leeeets gooooooo")
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxHeight(),
                    shape = RoundedCornerShape(5),
                    elevation = 10.dp
                ) {
                    if (!loading) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues = it)
                                .padding(top = 10.dp)
                        ) {
                            LazyColumn {
                                items(viewModel.friendList.size) { item ->
                                    FriendCard(
                                        image = R.drawable.shrek,
                                        viewModel.friendList[item],
                                        viewModel
                                    )
                                }
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    if (openDialog.value) {
                        DialogFriends(viewModel, openDialog, sharedPreferencesManager)
                    }
                }

            }
        }

    }
}

//Хз как там фотку передвать, пока айдишником
@Composable
fun FriendCard(image: Int, friendResponse: FriendResponse, viewModel: UserViewModel) {
    val context = LocalContext.current

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(100.dp)
                .padding(4.dp),
            elevation = 10.dp,
            shape = RoundedCornerShape(5.dp),
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Row(
                modifier = Modifier.clickable {
                    //Прокидываем инфу о друге
                    val intent = Intent(context, DetailFriendActivity::class.java)
                    intent.putExtra("friend", Gson().toJson(friendResponse))
                    context.startActivity(intent)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(80.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = image),
                    contentDescription = ""
                )
                Column() {
                    Text(
                        text = friendResponse.username,
                        style = MaterialTheme.typography.h1,
                        fontSize = 28.sp,
                        color = Color.White
                    )
                    OutlinedButton(
                        onClick = {
                            viewModel.deleteFriend(friendResponse.id)
                            viewModel.getFriends()
                        },
                        shape = RoundedCornerShape(15.dp),
                        border = BorderStroke(2.dp, Red500)
                    ) {
                        Text(
                            "Удалить",
                            style = MaterialTheme.typography.h1,
                            color = Red500,
                            fontSize = 20.sp
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun DialogFriends(
    viewModel: UserViewModel,
    openDialog: MutableState<Boolean>,
    sharedPreferencesManager: SharedPreferencesManager
) {
    val userState by viewModel.allUsersState.collectAsState(initial = UserState.Loading)
    val friendsRequestState by viewModel.friendsRequestState.collectAsState(initial = FriendsRequestState.Loading)
    val friendsOutputRequestState by viewModel.friendsOutputRequestState.collectAsState(initial = FriendsRequestState.Loading)

    // если нет друзей в лэйзи колумн, то выведи какую-нибудь картинку что тип некого добавлять
    // если count == 0 то ставь картинку о том, что все добавлены
    Dialog(
        onDismissRequest = { openDialog.value = false }
    ) {
        var count: Int = 0
        Surface(
            shape = RoundedCornerShape(5),
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp)
        ) {
            if (userState is UserState.Success
                && friendsRequestState is FriendsRequestState.Success
                && friendsOutputRequestState is FriendsRequestState.Success
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewModel.allUsersList.size) { item ->
                        if (viewModel.allUsersList[item].username != sharedPreferencesManager.getVal(
                                "username"
                            )
                            && !viewModel.friendList.contains(viewModel.allUsersList[item])
                            && !checkRequests(
                                viewModel.friendsRequestList,
                                viewModel.allUsersList[item].username
                            )
                            && !checkRequests(
                                viewModel.friendsOutputRequestList,
                                viewModel.allUsersList[item].username
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                count++
                                Text(viewModel.allUsersList[item].username, fontSize = 20.sp)
                                Button(onClick = {
                                    viewModel.sendFriendRequest(viewModel.allUsersList[item].id)
                                    viewModel.getFriendRequests()
                                    viewModel.getUsers()
                                }, modifier = Modifier.height(35.dp)) {
                                    Text("Добавить в друзья")
                                }
                            }
                            Divider(
                                Modifier
                                    .height(2.dp)
                                    .fillMaxWidth()
                            )
                        }
                        if (viewModel.allUsersList[item] == viewModel.allUsersList.last() && count == 0) {
                            count++
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier.clip(RoundedCornerShape(5.dp)),
                                    painter = painterResource(id = R.drawable.no_friends),
                                    contentDescription = "Lox"
                                )
                            }
                        }
                    }
                }
            } else
                if (userState is UserState.Loading
                    || friendsRequestState is FriendsRequestState.Loading
                    || friendsOutputRequestState is FriendsRequestState.Loading
                ) {
                    CircularProgressIndicator()
                } else {
                    Text("Что-то в этой жизни пошло не так")
                }
        }


    }


}

fun checkRequests(request: List<FriendRequestResponse>, username: String): Boolean {
    for (item in request) {
        println(item.from_user + " " + item.to_user)
        if (item.from_user == username || item.to_user == username) {
            return true
        }
    }
    return false
}

