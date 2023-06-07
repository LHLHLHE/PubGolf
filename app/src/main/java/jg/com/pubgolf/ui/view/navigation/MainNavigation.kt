package jg.com.pubgolf.ui.view.navigation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jg.com.pubgolf.ui.theme.Green500
import jg.com.pubgolf.ui.view.CreateGameActivity
import jg.com.pubgolf.ui.view.FriendsRequestActivity
import jg.com.pubgolf.ui.view.navigation.screens.HistoryScreen
import jg.com.pubgolf.ui.view.navigation.screens.ProfileScreen
import jg.com.pubgolf.viewModel.GameViewModel
import jg.com.pubgolf.viewModel.UserViewModel
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainNavigation(viewModel: UserViewModel, gameViewModel: GameViewModel) {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("PubGolf") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO как нибудь потом*/ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                })
        },

        bottomBar = { MyBottomBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // FAB onClick
                    /*
                        TODO
                            Здесь запускается новое активити
                            Но не трогай наверн пока, сам чуть позже сделаю
                     */
                    context.startActivity(
                        Intent(
                            context,
                            CreateGameActivity::class.java
                        )
                    )
                },
                modifier = Modifier
                    .border(
                        width = 3.dp,
                        color = Green500,
                        shape = CircleShape
                    )
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        scaffoldState = scaffoldState,
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center
    ) {

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /* TODO
                        полагаю, что здесь будут истории игр
                     */
                    HistoryScreen(gameViewModel)
                }
            }
            composable("profile") {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /* TODO
                        полагаю, что здесь будет профиль игрока
                     */
                    ProfileScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun MyBottomBar(navController: NavController) {
    val bottomMenuItemsList = prepareBottomMenu()

    var selectedItem by remember {
        mutableStateOf("Home")
    }

    BottomAppBar(
        cutoutShape = CircleShape
    ) {
        bottomMenuItemsList.forEachIndexed { index, menuItem ->
            if (index == 1) {
                // add an empty space for FAB
                BottomNavigationItem(
                    selected = false,
                    onClick = {},
                    icon = {},
                    enabled = false
                )
            }

            BottomNavigationItem(
                selected = (selectedItem == menuItem.label),
                onClick = {
                    selectedItem = menuItem.label

                    // Navigate to the corresponding destination
                    navController.navigate(menuItem.label.lowercase(Locale.ROOT))
                },
                icon = {
                    Icon(
                        imageVector = menuItem.icon,
                        contentDescription = menuItem.label
                    )
                },
                enabled = true
            )
        }
    }
}

private fun prepareBottomMenu(): List<BottomMenuItem> {
    val bottomMenuItemsList = arrayListOf<BottomMenuItem>()

    // add menu items
    bottomMenuItemsList.add(BottomMenuItem(label = "Home", icon = Icons.Filled.Home))
    bottomMenuItemsList.add(BottomMenuItem(label = "Profile", icon = Icons.Filled.Person))

    return bottomMenuItemsList
}

