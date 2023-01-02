package mago.apps.hertz.ui.screens.answer.text.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.input.CustomTextField
import mago.apps.hertz.ui.components.input.ITextCallback
import mago.apps.hertz.ui.components.input.KeyBoardActionUnit
import mago.apps.hertz.ui.screens.answer.text.AnswerTextViewModel
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@Composable
fun TagInfo(modifier: Modifier, answerTextViewModel: AnswerTextViewModel) {

    answerTextViewModel.tagListScrollState = rememberLazyListState()

    ScrollToBottomAction(answerTextViewModel)
    ScrollToEndAction(answerTextViewModel)

    TagTitle(modifier, answerTextViewModel)

    TagList(answerTextViewModel)

    InputTag(modifier, answerTextViewModel)

}

@Composable
private fun TagTitle(modifier: Modifier, answerTextViewModel: AnswerTextViewModel) {

    val tagList = answerTextViewModel.tagList

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.answer_text_title_tag),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Text(
            modifier = Modifier.padding(start = 6.dp),
            text = "(${tagList.size}/5)",
            color = if (tagList.size == 5) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun TagList(answerTextViewModel: AnswerTextViewModel) {

    val tagList = answerTextViewModel.tagList
    val tagScrollState = answerTextViewModel.tagListScrollState

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = tagScrollState
    ) {
        itemsIndexed(
            items = tagList,
            key = { _, item -> item }) { _, item ->
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .wrapContentWidth()
                    .height(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxHeight()
                        .noDuplicationClickable {
                            answerTextViewModel.removeTag(item)
                        }
                        .padding(6.dp),
                    imageVector = Icons.Filled.Close,
                    contentDescription = null, tint = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                    text = "#${item}",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun InputTag(modifier: Modifier, answerTextViewModel: AnswerTextViewModel) {
    var currentInputText = remember { "" }
    val tagList = answerTextViewModel.tagList

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "#",
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.titleLarge,
            )
            CustomTextField(
                modifier = Modifier.padding(horizontal = 8.dp),
                enable = tagList.size != 5,
                textStyle = MaterialTheme.typography.bodyLarge,
                textAlignment = Alignment.CenterStart,
                placeholderText = {
                    Text(
                        text = if (tagList.size != 5) {
                            stringResource(id = R.string.answer_text_placeholder)
                        } else {
                            stringResource(id = R.string.answer_text_title_tag_max_placeholder)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        textAlign = TextAlign.Center
                    )
                },
                iTextCallback = object : ITextCallback {
                    override fun renderText(content: String) {
                        currentInputText = content
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyBoardActionUnit = KeyBoardActionUnit(
                    onDone = {
                        answerTextViewModel.run {
                            addTag(currentInputText)
                            updateScrollBottomAction(true)
                            updateScrollEndAction(true)
                        }
                        currentInputText = ""
                    },
                ),
                actionDoneAfterClearText = true,
                textLimit = 6
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.tertiary)
        )

        Spacer(modifier = Modifier.height(60.dp))
    }


}

@Composable
private fun ScrollToBottomAction(answerTextViewModel: AnswerTextViewModel) {
    val scrollToBottomActionIsActive =
        answerTextViewModel.scrollToBottomAction.collectAsState().value

    if (scrollToBottomActionIsActive) {
        LaunchedEffect(key1 = true) {
            answerTextViewModel.scrollToBottom()
        }
    }
}


@Composable
private fun ScrollToEndAction(answerTextViewModel: AnswerTextViewModel) {
    val scrollToEndActionIsActive =
        answerTextViewModel.scrollToEndAction.collectAsState().value

    if (scrollToEndActionIsActive) {
        LaunchedEffect(key1 = true) {
            answerTextViewModel.scrollToEnd()
        }
    }
}
