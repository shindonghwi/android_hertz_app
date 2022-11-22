package mago.apps.hertz.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import mago.apps.hertz.R
import mago.apps.hertz.ui.screens.components.appbar.AppBar
import mago.apps.hertz.ui.screens.components.appbar.AppbarType
import mago.apps.hertz.ui.screens.components.button.FillButton
import mago.apps.hertz.ui.theme.Display2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        AppBar(type = AppbarType.EMPTY)
    }) {
        Box(modifier = Modifier.padding(it)) {
            HomeContent(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun HomeContent(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val imageHeight = configuration.screenHeightDp.dp * 0.1f

    ConstraintLayout(
        modifier = modifier, constraintSet = createConstraintSet()
    ) {
        Text(
            modifier = Modifier.layoutId("title"),
            text = stringResource(id = R.string.home_title),
            style = MaterialTheme.typography.Display2,
            color = MaterialTheme.colorScheme.primary
        )
        Column(
            modifier = Modifier.layoutId("profileImage"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(imageHeight)
                    .border(1.dp, Color.Black, CircleShape),
                imageVector = Icons.Rounded.Person,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = R.string.company),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        FillButton(
            modifier = Modifier
                .layoutId("loginButton")
                .padding(horizontal = 20.dp),
            content = stringResource(id = R.string.login),
            isEnabled = true,
        ) {
            Toast.makeText(context, "로그인 클릭", Toast.LENGTH_SHORT).show()
        }

    }
}

private fun createConstraintSet() = ConstraintSet {
    val title = createRefFor("title")
    val profileImage = createRefFor("profileImage")
    val loginButton = createRefFor("loginButton")

    constrain(title) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        linkTo(top = parent.top, bottom = parent.bottom, bias = 0.3f)
    }

    constrain(profileImage) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        linkTo(top = title.bottom, bottom = loginButton.top, bias = 0.5f)
    }

    constrain(loginButton) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        linkTo(top = parent.top, bottom = parent.bottom, bias = 0.6f)
    }

}