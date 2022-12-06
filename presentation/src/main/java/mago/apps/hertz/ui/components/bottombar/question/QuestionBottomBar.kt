package mago.apps.hertz.ui.components.bottombar.question

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
fun QuestionBottomBar(navController: NavHostController) {
    // 바텀바 높이
    val configuration = LocalConfiguration.current
    val bottomHeight = configuration.screenHeightDp.dp * 0.4f

    val isShowingDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(bottomHeight),
    ) {
        // 텍스트로 답하기(축소)
        QuestionBottomBarTextAnswer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.background)
                .noDuplicationClickable {
                    isShowingDialog.value = true
                },
        )

        // 음성으로 답하기(축소)
        QuestionBottomBarAudioAnswer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary)
                .noDuplicationClickable {
                    navController.navigate(RouteScreen.AnswerTextScreen.route)
                },
        )
    }

    if (isShowingDialog.value) {
        CustomPopup(PopupType.PERMISSION, object : PopupCallback {
            override fun onState(isVisible: Boolean) {
                if (!isVisible) {
                    isShowingDialog.value = false
                }
            }
        })
    }
}