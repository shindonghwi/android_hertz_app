package mago.apps.hertz.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import mago.apps.hertz.R
import mago.apps.hertz.ui.data.model.RouteScreen
import mago.apps.hertz.ui.screens.components.appbar.AppBar
import mago.apps.hertz.ui.screens.components.appbar.AppbarType
import mago.apps.hertz.ui.screens.components.button.ButtonColor
import mago.apps.hertz.ui.screens.components.button.FillButton
import mago.apps.hertz.ui.theme.backgroundColorSub

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        AppBar(type = AppbarType.EMPTY)
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColorSub)
                .padding(it)
        ) {
            HomeContent(
                modifier = Modifier.fillMaxSize(),
                navController = navController
            )
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val imageHeight = configuration.screenHeightDp.dp * 0.1f

    ConstraintLayout(
        modifier = modifier, constraintSet = createConstraintSet()
    ) {
        Text(
            modifier = Modifier.layoutId("title"),
            text = stringResource(id = R.string.home_title),
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.secondary
        )
        Column(
            modifier = Modifier.layoutId("profileImage"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(imageHeight),
                painter = painterResource(id = R.drawable.profile_sample),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = stringResource(id = R.string.company),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier
                .layoutId("buttonGroup")
                .padding(horizontal = 20.dp)
        ) {
            FillButton(
                content = stringResource(id = R.string.id),
                isEnabled = true,
                buttonColor = ButtonColor(
                    container = MaterialTheme.colorScheme.secondary,
                    disable = MaterialTheme.colorScheme.onSurfaceVariant,
                    content = MaterialTheme.colorScheme.onSecondary,
                    disabledContainer = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            ) {
                Toast.makeText(context, "아이디", Toast.LENGTH_SHORT).show()
            }

            FillButton(
                modifier = Modifier.padding(top = 16.dp),
                content = stringResource(id = R.string.pw),
                isEnabled = true,
                buttonColor = ButtonColor(
                    container = MaterialTheme.colorScheme.secondary.copy(alpha = 0.73f),
                    disable = MaterialTheme.colorScheme.onSurfaceVariant,
                    content = MaterialTheme.colorScheme.onSecondary,
                    disabledContainer = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            ) {
                Toast.makeText(context, "비밀번호", Toast.LENGTH_SHORT).show()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp),
                contentAlignment = Alignment.Center
            ) {
                FillButton(
                    modifier = Modifier.width(200.dp),
                    content = "시작하기",
                    isEnabled = true,
                    buttonColor = ButtonColor(
                        container = MaterialTheme.colorScheme.secondary,
                        disable = MaterialTheme.colorScheme.onSurfaceVariant,
                        content = MaterialTheme.colorScheme.onSecondary,
                        disabledContainer = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                ) {
                    /** TODO: 임시로 홈화면에서 질문화면으로 넘어가게 두었음*/
                    navController.navigate(RouteScreen.QuestionScreen.route)
                }
            }
        }

    }
}

private fun createConstraintSet() = ConstraintSet {
    val title = createRefFor("title")
    val profileImage = createRefFor("profileImage")
    val buttonGroup = createRefFor("buttonGroup")

    constrain(title) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        bottom.linkTo(profileImage.top, margin = 52.dp)
    }

    constrain(profileImage) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        linkTo(top = parent.top, bottom = parent.bottom, bias = 0.5f)
    }

    constrain(buttonGroup) {
        linkTo(start = parent.start, end = parent.end, bias = 0.5f)
        top.linkTo(profileImage.bottom, margin = 30.dp)
    }

}