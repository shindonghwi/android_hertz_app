package mago.apps.hertz.ui.screens.question.bottom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.hertz.ui.components.dialog.CustomPopup
import mago.apps.hertz.ui.components.dialog.PopupCallback
import mago.apps.hertz.ui.components.dialog.PopupType
import mago.apps.hertz.ui.navigation.model.RouteScreen
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@Composable
fun QuestionBottomBar(modifier: Modifier, navController: NavHostController) {
    val isShowingDialog = remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
    ) {
        QuestionBottomBarTextAnswer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.background)
                .noDuplicationClickable {
                    isShowingDialog.value = true
                },
        )
        QuestionBottomBarAudioAnswer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary)
                .noDuplicationClickable {
                    navController.navigate(RouteScreen.AnswerAudioScreen.route)
                },
        )
    }

    if (isShowingDialog.value) {
        CustomPopup(type = PopupType.PERMISSION, callback = object : PopupCallback {
            override fun onState(isVisible: Boolean) {
                if (!isVisible) {
                    isShowingDialog.value = false
                }
            }
        })
    }
}