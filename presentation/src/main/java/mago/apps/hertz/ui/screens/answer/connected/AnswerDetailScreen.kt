package mago.apps.hertz.ui.screens.answer.connected

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mago.apps.hertz.R
import mago.apps.hertz.ui.components.appbar.AppBarContent
import mago.apps.hertz.ui.screens.answer.common.QuestionContent
import mago.apps.hertz.ui.theme.light_sub_primary
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerConnectedScreen(
    navController: NavHostController,
    answerConnectedViewModel: AnswerConnectedViewModel,
    answerSeq: String?,
) {
    Scaffold(
        topBar = { AnswerConnectedAppBar(navController) },
    ) {
        AnswerConnectedContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
            answerConnectedViewModel = answerConnectedViewModel
        )
    }
}

@Composable
private fun AnswerConnectedContent(
    modifier: Modifier,
    answerConnectedViewModel: AnswerConnectedViewModel
) {
//    QuestionContent(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(200.dp)
//            .clip(RoundedCornerShape(9.dp))
//            .background(light_sub_primary)
//            .padding(14.dp),
//        content = answerTextViewModel.questionInfo?.text,
//    )
}


@Composable
private fun AnswerConnectedAppBar(
    navController: NavHostController,
) {
    AppBarContent(
        leftContent = {
            Icon(modifier = Modifier
                .size(40.dp)
                .noDuplicationClickable {
                    navController.popBackStack()
                }
                .padding(6.dp),
                imageVector = Icons.Default.ArrowBack,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null)
        },
        centerContent = {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = R.string.answer_connected_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    )
}