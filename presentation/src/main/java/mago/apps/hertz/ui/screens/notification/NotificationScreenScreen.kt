package mago.apps.hertz.ui.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun NotificationScreenScreen() {
    NotificationContent()
}

@Composable
private fun NotificationContent() {
    val dummyList = listOf<String>(
        "user1이 \"내가 어른이 됐다고 느낄 때는?\" 질문에 답변을 남겼습니다",
        "user2이 \"지금 듣고 싶은 노래는?\" 질문에 답변을 남겼습니다",
        "user3이 \"내가 어른이 됐다고 느낄 때는?\" 질문에 답변을 남겼습니다",
        "user4이 \"지금 듣고 싶은 노래는?\" 질문에 답변을 남겼습니다",
        "user5이 \"내가 어른이 됐다고 느낄 때는?\" 질문에 답변을 남겼습니다",
        "user6이 \"지금 듣고 싶은 노래는?\" 질문에 답변을 남겼습니다",
        "user7이 \"내가 어른이 됐다고 느낄 때는?\" 질문에 답변을 남겼습니다",
        "user8이 \"지금 듣고 싶은 노래는?\" 질문에 답변을 남겼습니다",
        "user9이 \"내가 어른이 됐다고 느낄 때는?\" 질문에 답변을 남겼습니다",
        "user10이 \"지금 듣고 싶은 노래는?\" 질문에 답변을 남겼습니다",
        "user1이 \"내가 어른이 됐다고 느낄 때는?\" 질문에 답변을 남겼습니다",
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(count = dummyList.size, key = { it }) {
            NotificationItem(agoTimeText = "1시간전", content = dummyList[it])
        }
    }
}

@Composable
private fun NotificationItem(agoTimeText: String, content: String) {

    val cardShape = RoundedCornerShape(10.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(
                elevation = 5.dp,
                shape = cardShape
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = cardShape,
            )
            .padding(vertical = 8.dp, horizontal = 15.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = agoTimeText,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.End
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .weight(1f),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = content,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
