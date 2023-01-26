package mago.apps.hertz.ui.components.dialog.alert

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ErrorMessageDialog(
    errorDialog: StateFlow<String>,
    confirmText: String? = null,
    dismissText: String? = null,
    dismissRequestEvent: (() -> Unit)? = null,
    confirmEvent: (() -> Unit)? = null,
    dismissEvent: (() -> Unit)? = null,
) {
    val errorDialogMessage = errorDialog.collectAsState().value

    if (errorDialogMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {
                dismissRequestEvent?.let { it() }
            },
            text = { Text(text = errorDialogMessage) },
            confirmButton = {
                TextButton(onClick = {
                    confirmEvent?.let { it() }
                }) {
                    confirmText?.let { Text(it) }
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    dismissEvent?.let { it() }
                }) {
                    dismissText?.let { Text(it) }
                }
            },
            shape = RoundedCornerShape(14.dp)
        )
    }

}
