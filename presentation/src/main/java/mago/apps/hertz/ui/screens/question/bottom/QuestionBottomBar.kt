package mago.apps.hertz.ui.screens.question.bottom

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import mago.apps.hertz.ui.components.dialog.CustomPopup
import mago.apps.hertz.ui.components.dialog.PopupPermissionCallback
import mago.apps.hertz.ui.components.dialog.PopupType
import mago.apps.hertz.ui.navigation.model.RouteScreen
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QuestionBottomBar(modifier: Modifier, navController: NavHostController) {
    val isShowingDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    )

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
                    if (permissionState.allPermissionsGranted) {
                        navController.navigate(RouteScreen.AnswerAudioScreen.route)
                    } else {
                        isShowingDialog.value = true
                    }
                },
        )
    }

    CustomPopup(
        isVisible = isShowingDialog,
        type = PopupType.PERMISSION,
        permissionCallback = object : PopupPermissionCallback {
            override fun allAllow() {
                isShowingDialog.value = false
                Toast.makeText(context, "allAllow", Toast.LENGTH_SHORT).show()
            }

            override fun deny() {
                isShowingDialog.value = false
                Toast.makeText(context, "deny", Toast.LENGTH_SHORT).show()
            }
        }
    )
}