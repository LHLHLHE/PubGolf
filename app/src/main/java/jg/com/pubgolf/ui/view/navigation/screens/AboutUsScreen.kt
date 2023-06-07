package jg.com.pubgolf.ui.view.navigation.screens

import android.R
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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


@Composable
fun AboutUsScreen(){
    val activity = LocalContext.current as Activity

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("О нас") },
                navigationIcon = {
                    IconButton(onClick = {
                        activity.finish()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                })
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "НЕТ, Я НЕ ОСТАВЛЮ ЗДЕСЬ МАТ, АРТЁМ")
            Image(
                painter = painterResource(id = jg.com.pubgolf.R.drawable.base),
                contentDescription = "я должен был",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(300.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
            )
        }
    }
}