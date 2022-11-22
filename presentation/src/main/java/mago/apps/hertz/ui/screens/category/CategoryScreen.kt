package mago.apps.hertz.ui.screens.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import mago.apps.hertz.R
import mago.apps.hertz.ui.screens.components.appbar.AppBar
import mago.apps.hertz.ui.screens.components.appbar.AppbarType
import mago.apps.hertz.ui.theme.backgroundColorSub
import mago.apps.hertz.ui.utils.compose.modifier.noDuplicationClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen() {
    Scaffold(topBar = {
        AppBar(type = AppbarType.ICON_TITLE_ICON,
            textContent = { CategoryAppBarTitleContent() },
            leftContent = { CategoryAppBarLeftContent() },
            rightContent = { CategoryAppBarRightContent() })
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColorSub)
                .padding(it)
        ) {
            CategoryContent(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun CategoryContent(modifier: Modifier) {
//    LazyRow(content = )
}

@Composable
private fun CategoryAppBarTitleContent() {
    Text(
        modifier = Modifier.padding(start = 8.dp),
        text = stringResource(id = R.string.home_title_2),
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
    )
}


@Composable
private fun CategoryAppBarLeftContent() {
    Icon(
        modifier = Modifier.size(44.dp),
        painter = painterResource(id = R.drawable.profile_sample),
        contentDescription = null,
        tint = Color.Unspecified
    )
}

@Composable
private fun CategoryAppBarRightContent() {
    Icon(modifier = Modifier
        .size(36.dp)
        .noDuplicationClickable {

        }
        .padding(6.dp),
        painter = painterResource(id = R.drawable.comment),
        contentDescription = null,
        tint = Color.Unspecified)
    Icon(modifier = Modifier
        .size(36.dp)
        .noDuplicationClickable {

        }
        .padding(6.dp),
        painter = painterResource(id = R.drawable.marker),
        contentDescription = null,
        tint = Color.Unspecified)
    Icon(modifier = Modifier
        .size(36.dp)
        .noDuplicationClickable {

        }
        .padding(6.dp),
        painter = painterResource(id = R.drawable.bell),
        contentDescription = null,
        tint = Color.Unspecified)
}

