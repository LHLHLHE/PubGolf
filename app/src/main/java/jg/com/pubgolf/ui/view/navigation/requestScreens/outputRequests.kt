package jg.com.pubgolf.ui.view.navigation.requestScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoveToInbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jg.com.pubgolf.ui.theme.Green500
import jg.com.pubgolf.ui.theme.Purple500
import jg.com.pubgolf.ui.theme.Red500
import jg.com.pubgolf.viewModel.UserViewModel
import jg.com.pubgolf.viewModel.state.FriendsRequestState

@Composable
fun outputRequests(viewModel: UserViewModel) {
    val friendsRequestState by viewModel.friendsOutputRequestState.collectAsState(initial = FriendsRequestState.Loading)

    LaunchedEffect(key1 = Unit) {
        viewModel.getFriendOutputRequests()
    }

    // TODO: нормальную карточку исходящего запроса, и мб картинку, если запросов нет
    // все запросы исходящие находятся в списке viewModel.friendsOutputRequestList
    if(friendsRequestState is FriendsRequestState.Success) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.friendsOutputRequestList.size) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(Modifier.height(30.dp))
                        Text(
                            viewModel.friendsOutputRequestList[item].to_user,
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.h1,
                            color = Purple500
                        )
                    }

                    Divider(Modifier.height(3.dp))
                }
            }
        }
    }
    else {
        CircularProgressIndicator()
    }
}