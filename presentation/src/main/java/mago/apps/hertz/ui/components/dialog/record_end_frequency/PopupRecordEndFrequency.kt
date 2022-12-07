package mago.apps.hertz.ui.components.dialog.record_end_frequency

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mago.apps.hertz.R
import mago.apps.hertz.ui.theme.TitleXLarge

@Composable
fun PopupRecordEndFrequency(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(68.dp))
            Icon(
                modifier = Modifier.size(52.dp),
                painter = painterResource(id = R.drawable.profile_sample),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mago 님의\n" + stringResource(id = R.string.dialog_record_end_frequency_title),
                style = MaterialTheme.typography.TitleXLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(id = R.string.dialog_record_end_frequency_subtitle),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight(700)),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                textAlign = TextAlign.Center
            )
        }

    }
}