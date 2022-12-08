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
import mago.apps.hertz.ui.theme.titleXLarge
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@Composable
fun EpisodeSaveScreen() {
    EpisodeSaveContent()
}

@Composable
private fun EpisodeSaveContent(isExistFrequency: Boolean = true) {
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

        if (isExistFrequency){
            FrequencyExistContent()
        }else{
            FrequencyNotExistContent()
        }
    }
}

@Composable
private fun FrequencyExistContent() {
    val menuList = listOf(
        Triple(first = stringResource(id = R.string.episode_save_button_result_list),
            second = R.drawable.list,
            third = {}),
    )
    Column(modifier = Modifier.padding(horizontal = 40.dp)) {
        menuList.forEach { item ->
            FrequencyMenuButton(item)
        }
        FrequencyCheckCard()
    }
}

@Composable
private fun FrequencyCheckCard() {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.episode_save_card_title),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        // 감정주파수
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(id = R.string.episode_save_card_keyword_frequency),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight(600)),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        // 헤찌뽕 키워드
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(id = R.string.episode_save_card_keyword_hezzibbong),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight(600)),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        // 자세히보기
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primary)
                .noDuplicationClickable {}, contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.episode_save_button_detail_view),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun FrequencyNotExistContent() {

    val menuList =
        listOf(Triple(first = stringResource(id = R.string.episode_save_button_keep_alone),
            second = R.drawable.lock,
            third = {}),
            Triple(first = stringResource(id = R.string.episode_save_button_noti_opponent),
                second = R.drawable.phone_msg,
                third = {}))

    Column(modifier = Modifier.padding(horizontal = 40.dp)) {
        menuList.forEach { item ->
            FrequencyMenuButton(item)
        }

        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = stringResource(id = R.string.episode_save_card_title),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight(800)),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FrequencyMenuButton(item: Triple<String, Int, () -> Unit>) {
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
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = item.second),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.background.copy(0.9f)
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