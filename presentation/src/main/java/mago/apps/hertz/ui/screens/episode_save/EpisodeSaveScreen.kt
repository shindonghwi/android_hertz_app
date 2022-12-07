package mago.apps.hertz.ui.screens.episode_save

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mago.apps.hertz.R
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@Composable
fun EpisodeSaveScreen() {
    EpisodeSaveContent()
}

@Composable
private fun EpisodeSaveContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.episode_save_title),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight(800)),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        FrequencyExistContent()
        FrequencyNotExistContent()
    }
}

@Composable
private fun FrequencyExistContent() {

}

@Composable
private fun FrequencyNotExistContent() {

    val menuList = listOf(
        Triple(
            first = stringResource(id = R.string.episode_save_button_keep_alone),
            second = R.drawable.lock,
            third = {}
        ),
        Triple(
            first = stringResource(id = R.string.episode_save_button_noti_opponent),
            second = R.drawable.phone_msg,
            third = {}
        )
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {

        menuList.forEach { item ->
            Row(
                modifier = Modifier
                    .padding(vertical = 14.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .noDuplicationClickable {
                        item.third()
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = item.second),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = item.first,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight(800)),
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )

            }

        }
    }

}
