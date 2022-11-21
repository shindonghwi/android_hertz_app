package mago.apps.hertz.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import mago.apps.hertz.ui.screens.components.appbar.AppBar
import mago.apps.hertz.ui.screens.components.appbar.AppbarType

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        AppBar(type = AppbarType.EMPTY)
    }) {
        Box(modifier = Modifier.padding(it)) {
            HomeContent(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow)
            )
        }
    }
}

@Composable
private fun HomeContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("hihi")
    }
}