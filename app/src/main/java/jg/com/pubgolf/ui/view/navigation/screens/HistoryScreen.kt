package jg.com.pubgolf.ui.view.navigation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jg.com.pubgolf.data.model.GameModels.GameResponse
import jg.com.pubgolf.ui.theme.Purple500
import jg.com.pubgolf.utils.convertDateTime
import jg.com.pubgolf.viewModel.GameViewModel
import jg.com.pubgolf.viewModel.state.GameState

@Composable
fun HistoryScreen(gameViewModel: GameViewModel) {

    val context = LocalContext.current
    val gamesState by gameViewModel.allGamesState.collectAsState(initial = GameState.Loading)
    val loading = remember { mutableStateOf(false) }

    //gameViewModel.getGames() Пофикси фигню с тем, что каждый раз вызывается эта фигня, т.к.
    // перерисовывается экран миллиард раз, я просто не придумал как сделать логику
    //так, чтобы один раз ток вызвалась

    LaunchedEffect(gamesState) {
        when (gamesState) {
            is GameState.Loading -> {
                gameViewModel.getGames() // Обновляет один раз, если создать игру не обновит
                loading.value = false
            }
            is GameState.Success -> {
                loading.value = true
            }
            is GameState.Error -> {
                gameViewModel.getGames()
                loading.value = false
                Toast.makeText(context, "Ошибка получения игр!", Toast.LENGTH_LONG).show()
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight(0.92f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loading.value) {
            items(gameViewModel.allGamesList.size) { item ->
                GameCard(gameViewModel.allGamesList[item])
            }
        }
    }
    Spacer(modifier = Modifier.height(100.dp))
}

@Composable
fun GameCard(game: GameResponse) {
    val cardColor = remember { mutableStateOf(Color(0x332F3273)) }
    val gameStatus = remember { mutableStateOf("В процессе") }

    if (game.status == "started") {
        cardColor.value = Color(0x33BFFF09)
    } else if (game.status == "finished") {
        cardColor.value = Color(0x33F20202)
        gameStatus.value = convertDateTime(game.finish_time)
    }

    Card(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, cardColor.value),
        backgroundColor = cardColor.value,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(150.dp)
            .padding(10.dp)
            .clickable {
                //ВНУТРЬ ИГРЫ
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(

            ) {
                //НУ ДА, ГОВОНОКОД И ЧО ТЫ МНЕ СДЕЛАЕШЬ?? Я В ДРУГОМ ГОРОДЕ
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.h1,
                    color = Purple500,
                    fontSize = 20.sp
                )
                Text(
                    text = "Сложность: " + game.difficulty_level,
                    style = MaterialTheme.typography.h1,
                    color = Purple500,
                    fontSize = 16.sp
                )
                Text(
                    text = "Бюджет: " + game.budget_level,
                    style = MaterialTheme.typography.h1,
                    color = Purple500,
                    fontSize = 16.sp
                )
                Text(
                    text = "Старт: " + convertDateTime(game.start_time),
                    style = MaterialTheme.typography.h1,
                    color = Purple500,
                    fontSize = 16.sp
                )
                Text(
                    text = "Конец: " + gameStatus.value,
                    style = MaterialTheme.typography.h1,
                    color = Purple500,
                    fontSize = 16.sp
                )
            }
            //Можно впиндюрить кнопку какую-нибудь
        }
    }
}