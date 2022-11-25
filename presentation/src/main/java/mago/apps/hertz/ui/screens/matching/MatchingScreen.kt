package mago.apps.hertz.ui.screens.matching

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import mago.apps.hertz.R
import mago.apps.hertz.ui.data.model.RouteScreen
import mago.apps.hertz.ui.screens.components.appbar.AppBar
import mago.apps.hertz.ui.screens.components.appbar.AppbarType
import mago.apps.hertz.ui.screens.components.button.ButtonColor
import mago.apps.hertz.ui.screens.components.button.FillButton
import mago.apps.hertz.ui.theme.light_sub_primary
import mago.apps.hertz.ui.theme.light_sub_primary_background
import mago.apps.hertz.ui.theme.light_sub_primary_than_darker_2
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchingScreen(navController: NavHostController) {
    Scaffold(topBar = {
        AppBar(type = AppbarType.EMPTY)
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isSystemInDarkTheme()) {
                        light_sub_primary_background
                    } else {
                        /** TODO: 매칭 상대 입력 화면 다크 색상 지정 필요 */
                        light_sub_primary_background
                    }
                )
                .padding(it)
        ) {
            MatchingContent(
                modifier = Modifier.fillMaxSize(), navController = navController
            )
        }
    }
}

@Composable
private fun MatchingContent(
    modifier: Modifier = Modifier, navController: NavController
) {
    val context = LocalContext.current

    ConstraintLayout(
        modifier = modifier, constraintSet = createConstraintSet()
    ) {
        Text(
            modifier = Modifier.layoutId("title"),
            text = stringResource(id = R.string.matching_title),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = if (isSystemInDarkTheme()) {
                light_sub_primary_than_darker_2
            } else {
                /** TODO: 로그인 화면 다크 색상 지정 필요 */
                light_sub_primary_than_darker_2
            },
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier
                .layoutId("buttonGroup")
                .padding(horizontal = 20.dp)
        ) {
            FillButton(
                content = stringResource(id = R.string.phone),
                isEnabled = true,
                buttonColor = ButtonColor(
                    container = if (isSystemInDarkTheme()) {
                        light_sub_primary.copy(alpha = 0.73f)
                    } else {
                        light_sub_primary.copy(alpha = 0.73f)
                    },
                    disable = if (isSystemInDarkTheme()) {
                        Color.White.copy(alpha = 0.3f)
                    } else {
                        Color.White.copy(alpha = 0.3f)
                    },
                    content = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.White
                    },
                    disabledContainer = if (isSystemInDarkTheme()) {
                        light_sub_primary.copy(alpha = 0.3f)
                    } else {
                        light_sub_primary.copy(alpha = 0.3f)
                    },
                ),
                tailIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(36.dp)
                            .noDuplicationClickable {
                                navController.navigate(RouteScreen.QuestionScreen.route)
                            }
                            .padding(6.dp),
                        painter = painterResource(id = R.drawable.arrow_circle_up),
                        tint = Color.White,
                        contentDescription = null,
                    )
                },
            ) {
                Toast.makeText(context, "전화번호", Toast.LENGTH_SHORT).show()
            }

            FillButton(
                modifier = Modifier.padding(top = 16.dp),
                content = stringResource(id = R.string.skip),
                isEnabled = true,
                buttonColor = ButtonColor(
                    container = if (isSystemInDarkTheme()) {
                        light_sub_primary
                    } else {
                        light_sub_primary
                    },
                    disable = if (isSystemInDarkTheme()) {
                        Color.White.copy(alpha = 0.3f)
                    } else {
                        Color.White.copy(alpha = 0.3f)
                    },
                    content = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.White
                    },
                    disabledContainer = if (isSystemInDarkTheme()) {
                        light_sub_primary.copy(alpha = 0.3f)
                    } else {
                        light_sub_primary.copy(alpha = 0.3f)
                    },
                ),
                tailIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(36.dp)
                            .noDuplicationClickable {
                                navController.navigate(RouteScreen.QuestionScreen.route)
                            }
                            .padding(6.dp),
                        painter = painterResource(id = R.drawable.arrow_circle_up),
                        tint = Color.White,
                        contentDescription = null,
                    )
                },
            ) {
                Toast.makeText(context, "넘아가기", Toast.LENGTH_SHORT).show()
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