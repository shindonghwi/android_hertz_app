package mago.apps.hertz.ui.screens.question.bottom

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.gson.Gson
import mago.apps.hertz.ui.components.dialog.CustomPopup
import mago.apps.hertz.ui.components.dialog.PopupPermissionCallback
import mago.apps.hertz.ui.components.dialog.PopupType
import mago.apps.hertz.ui.model.screen.RouteScreen
import mago.apps.hertz.ui.navigation.navigateTo
import mago.apps.hertz.ui.screens.question.QuestionViewModel
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QuestionBottomBar(
    modifier: Modifier,
    navController: NavHostController,
    questionViewModel: QuestionViewModel
) {
    val isPermissionPopUpVisible = remember { mutableStateOf(false) }

    val permissionList: ArrayList<String> = arrayListOf<String>().apply {
        add(android.Manifest.permission.RECORD_AUDIO)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val permissionState = rememberMultiplePermissionsState(permissionList.toList())

    Column(
        modifier = modifier,
    ) {
        QuestionBottomBarTextAnswer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.background)
                .noDuplicationClickable {
                    navController.navigateTo(
                        RouteScreen.AnswerTextScreen.route +
                                "?question=${Gson().toJson(questionViewModel.questionInfo)}"
                    )
                },
            questionViewModel = questionViewModel
        )
        QuestionBottomBarAudioAnswer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary)
                .noDuplicationClickable {
                    if (permissionState.allPermissionsGranted) {
                        navController.navigateTo(
                            RouteScreen.AnswerAudioScreen.route +
                                    "?question=${Gson().toJson(questionViewModel.questionInfo)}"
                        )
                    } else {
                        isPermissionPopUpVisible.value = true
                    }
                },
        )
    }

    CustomPopup(
        isVisible = isPermissionPopUpVisible,
        type = PopupType.PERMISSION,
        permissionCallback = object : PopupPermissionCallback {
            override fun allAllow() {
                isPermissionPopUpVisible.value = false
            }

            override fun deny() {
                isPermissionPopUpVisible.value = false
            }
        }
    )
}