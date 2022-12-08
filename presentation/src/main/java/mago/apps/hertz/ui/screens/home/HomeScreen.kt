package mago.apps.hertz.ui.screens.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.input.CustomTextField
import mago.apps.hertz.ui.components.input.KeyBoardActionUnit
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@Composable
fun HomeScreen(navController: NavController) {
    HomeContent(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier, navController: NavController
) {
    val configuration = LocalConfiguration.current
    val imageHeight = configuration.screenHeightDp.dp * 0.1f

    ConstraintLayout(
        modifier = modifier, constraintSet = createConstraintSet()
    ) {
        Text(
            modifier = Modifier.layoutId("title"),
            text = stringResource(id = R.string.home_title),
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
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
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Column(
            modifier = Modifier
                .layoutId("buttonGroup")
                .padding(horizontal = 20.dp)
        ) {
            InputId()
            InputPw(navController)
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

@Composable
private fun InputId() {

    val focused = remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current

    CustomTextField(
        modifier = Modifier
            .onFocusChanged {
                focused.value = it.hasFocus
            }
            .padding(top = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp, color = if (focused.value) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }, shape = RoundedCornerShape(16.dp)
            ),
        innerTextPaddingValues = PaddingValues(start = 12.dp),
        trailingPaddingValues = PaddingValues(end = 8.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
        ),
        keyBoardActionUnit = KeyBoardActionUnit(onNext = {
            localFocusManager.moveFocus(FocusDirection.Down)
        }),
        placeholderText = {
            Text(
                text = stringResource(id = R.string.id),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary.copy(0.4f),
                textAlign = TextAlign.Center
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary),
        textAlignment = Alignment.CenterStart
    )
}


@Composable
private fun InputPw(navController: NavController) {
    val focused = remember { mutableStateOf(false) }

    CustomTextField(modifier = Modifier
        .onFocusChanged {
            focused.value = it.hasFocus
        }
        .padding(top = 16.dp)
        .fillMaxWidth()
        .height(48.dp)
        .clip(RoundedCornerShape(16.dp))
        .border(
            width = 1.dp, color = if (focused.value) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }, shape = RoundedCornerShape(16.dp)
        ),
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .padding(end = 0.dp)
                    .size(36.dp)
                    .noDuplicationClickable {
                        navController.navigate(RouteScreen.QuestionScreen.route)
                    }
                    .padding(6.dp),
                painter = painterResource(id = R.drawable.arrow_circle_up),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                contentDescription = null,
            )
        },
        innerTextPaddingValues = PaddingValues(start = 12.dp),
        trailingPaddingValues = PaddingValues(end = 8.dp),
        placeholderText = {
            Text(
                text = stringResource(id = R.string.pw),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary.copy(0.4f),
                textAlign = TextAlign.Center
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary),
        textAlignment = Alignment.CenterStart
    )
}
