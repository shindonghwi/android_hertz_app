package mago.apps.hertz.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import mago.apps.hertz.ui.screens.components.input.CustomTextField
import mago.apps.hertz.ui.screens.components.input.CustomTextField1
import mago.apps.hertz.ui.theme.light_sub_primary
import mago.apps.hertz.ui.theme.light_sub_primary_background
import mago.apps.hertz.ui.theme.light_sub_primary_than_darker_1
import mago.apps.hertz.ui.theme.light_sub_secondary
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
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
                        /** TODO: 로그인 화면 다크 색상 지정 필요 */
                        light_sub_primary_background
                    }
                )
                .padding(it)
        ) {
            HomeContent(
                modifier = Modifier.fillMaxSize(), navController = navController
            )
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier, navController: NavController
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
            color = if (isSystemInDarkTheme()) {
                light_sub_primary_than_darker_1
            } else {
                /** TODO: 로그인 화면 다크 색상 지정 필요 */
                light_sub_primary_than_darker_1
            }
        )
        Column(
            modifier = Modifier.layoutId("profileImage"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(imageHeight),
                painter = painterResource(id = R.drawable.profile_sample),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = stringResource(id = R.string.company),
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSystemInDarkTheme()) {
                    light_sub_secondary
                } else {
                    /** TODO: 로그인 화면 다크 색상 지정 필요 */
                    light_sub_secondary
                }
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
                )
            ) {
                Toast.makeText(context, "아이디", Toast.LENGTH_SHORT).show()
            }

            CustomTextField1()
//            CustomTextField(
//                modifier = Modifier
//                    .padding(top = 16.dp)
//                    .fillMaxWidth()
//                    .height(48.dp)
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(light_sub_primary),
//                textColor = Color.White,
//                textAlignment = Arrangement.Start,
//                trailingIcon = {
//                    Icon(
//                        modifier = Modifier
//                            .padding(end = 8.dp)
//                            .size(36.dp)
//                            .noDuplicationClickable {
//                                navController.navigate(RouteScreen.MatchingScreen.route)
//                            }
//                            .padding(6.dp),
//                        painter = painterResource(id = R.drawable.arrow_circle_up),
//                        tint = Color.White,
//                        contentDescription = null,
//                    )
//                }
//            )
//            FillButton(
//                modifier = Modifier.padding(top = 16.dp),
//                content = stringResource(id = R.string.pw),
//                isEnabled = true,
//                buttonColor = ButtonColor(
//                    container = if (isSystemInDarkTheme()) {
//                        light_sub_primary.copy(alpha = 0.73f)
//                    } else {
//                        light_sub_primary.copy(alpha = 0.73f)
//                    },
//                    disable = if (isSystemInDarkTheme()) {
//                        Color.White.copy(alpha = 0.3f)
//                    } else {
//                        Color.White.copy(alpha = 0.3f)
//                    },
//                    content = if (isSystemInDarkTheme()) {
//                        Color.White
//                    } else {
//                        Color.White
//                    },
//                    disabledContainer = if (isSystemInDarkTheme()) {
//                        light_sub_primary.copy(alpha = 0.3f)
//                    } else {
//                        light_sub_primary.copy(alpha = 0.3f)
//                    },
//                ),
//                tailIcon = {
//                    Icon(
//                        modifier = Modifier
//                            .padding(end = 8.dp)
//                            .size(36.dp)
//                            .noDuplicationClickable {
//                                navController.navigate(RouteScreen.MatchingScreen.route)
//                            }
//                            .padding(6.dp),
//                        painter = painterResource(id = R.drawable.arrow_circle_up),
//                        tint = Color.White,
//                        contentDescription = null,
//                    )
//                }
//            ) {
//                Toast.makeText(context, "비밀번호", Toast.LENGTH_SHORT).show()
//            }
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