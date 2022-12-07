package mago.apps.hertz.ui.components.dialog.permission

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.dialog.PopupPermissionCallback
import mago.apps.hertz.ui.theme.TitleXLarge
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable
import mago.apps.hertz.ui.utils.permission.PermissionsHandler

@Composable
fun PopupPermission(modifier: Modifier, permissionCallback: PopupPermissionCallback?) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
            text = stringResource(id = R.string.permission_mic_storage),
            style = MaterialTheme.typography.TitleXLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PopupPermissionButton(
                content = stringResource(id = R.string.allow),
                callback = permissionCallback,
            )
            PopupPermissionButton(
                content = stringResource(id = R.string.deny),
                buttonAlpha = 0.6f,
                callback = permissionCallback
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PopupPermissionButton(
    content: String, buttonAlpha: Float = 1f, callback: PopupPermissionCallback?
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissions = remember {
        listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
    val permissionsHandler = remember(permissions) { PermissionsHandler() }
    val permissionsStates by permissionsHandler.state.collectAsState()
    HandlePermissionsRequest(permissions = permissions, permissionsHandler = permissionsHandler)

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    permissionsStates.multiplePermissionsState
                    if (permissionsHandler.isAllGranted(permissions)) {
                        callback?.allAllow()
                    } else {
                        permissionsHandler.onEvent(PermissionsHandler.Event.Nothing)
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = buttonAlpha))
            .noDuplicationClickable {
                callback?.run {
                    if (content == context.resources.getString(R.string.allow)) {
                        if (permissionsHandler.isAllGranted(permissions)) {
                            allAllow()
                        } else {
                            permissionsHandler.onEvent(PermissionsHandler.Event.PermissionRequired)
                        }
                    } else {
                        deny()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandlePermissionsRequest(permissions: List<String>, permissionsHandler: PermissionsHandler) {

    val state by permissionsHandler.state.collectAsState()
    val permissionsState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(permissionsState) {
        permissionsHandler.onEvent(PermissionsHandler.Event.PermissionsStateUpdated(permissionsState))
        when {
            permissionsState.allPermissionsGranted -> {
                permissionsHandler.onEvent(PermissionsHandler.Event.PermissionsGranted)
            }
            permissionsState.permissionRequested && !permissionsState.shouldShowRationale -> {
                permissionsHandler.onEvent(PermissionsHandler.Event.PermissionNeverAskAgain)
            }
            else -> {
                permissionsHandler.onEvent(PermissionsHandler.Event.PermissionDenied)
            }
        }
    }

    HandlePermissionAction(
        action = state.permissionAction,
        permissionStates = state.multiplePermissionsState,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandlePermissionAction(
    action: PermissionsHandler.Action,
    permissionStates: MultiplePermissionsState?,
) {
    val context = LocalContext.current
    when (action) {
        PermissionsHandler.Action.REQUEST_PERMISSION -> {
            LaunchedEffect(true) {
                permissionStates?.launchMultiplePermissionRequest()
            }
        }
        PermissionsHandler.Action.SHOW_RATIONALE -> {
            LaunchedEffect(true) {
                permissionStates?.launchMultiplePermissionRequest()
            }
        }
        PermissionsHandler.Action.SHOW_NEVER_ASK_AGAIN -> {
            Toast.makeText(
                context,
                context.getString(R.string.toast_permission_required),
                Toast.LENGTH_SHORT
            ).show()
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:" + context.packageName)
                context.startActivity(this)
            }
        }
        PermissionsHandler.Action.NO_ACTION -> Unit
    }
}
