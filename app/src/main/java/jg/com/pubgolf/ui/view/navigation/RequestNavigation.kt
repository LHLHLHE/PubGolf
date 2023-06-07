package jg.com.pubgolf.ui.view.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jg.com.pubgolf.ui.theme.Green500
import jg.com.pubgolf.ui.view.navigation.requestScreens.inputRequests
import jg.com.pubgolf.ui.view.navigation.requestScreens.outputRequests
import jg.com.pubgolf.ui.view.navigation.screens.HistoryScreen
import jg.com.pubgolf.ui.view.navigation.screens.ProfileScreen
import jg.com.pubgolf.viewModel.UserViewModel
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RequestNavigation(viewModel: UserViewModel) {
    val scaffoldState = rememberScaffoldState()
    val navRequestsController = rememberNavController()


    Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = { MyBottomRequestBar(navRequestsController) },
    scaffoldState = scaffoldState,
    ) {
        NavHost(navController = navRequestsController, startDestination = "inputRequests") {
            composable("inputRequests") {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    inputRequests(viewModel)

                }
            }
            composable("outputRequests") {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    outputRequests(viewModel)
                }
            }
        }
    }
}


@Composable
fun MyBottomRequestBar(navController: NavController) {
    val bottomMenuItemsList = prepareBottomRequestMenu()

    var selectedItem by remember {
        mutableStateOf("inputRequests")
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

private fun prepareBottomRequestMenu(): List<BottomMenuItem> {
    val bottomMenuItemsList = arrayListOf<BottomMenuItem>()

    // add menu items
    bottomMenuItemsList.add(BottomMenuItem(label = "inputRequests", icon = Icons.Filled.PersonAdd))
    bottomMenuItemsList.add(BottomMenuItem(label = "outputRequests", icon = Icons.Filled.PersonAddAlt1))

    return bottomMenuItemsList
}
