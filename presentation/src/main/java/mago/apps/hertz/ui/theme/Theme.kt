package mago.apps.hertz.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Primary40, // 기본 색상은 앱에서 가장 자주 표시되는 색상입니다.
    primaryVariant = Primary90, // 기본 변형 색상은 두 요소를 구별하는 데 사용됩니다.
    onPrimary = White, // 주색 위에 표시되는 텍스트 및 아이콘에 사용되는 색상입니다.
    secondary = Secondary40, // 두 번째 색상은 당신을 강조하고 구별할 수 있는 더 많은 방법을 제공합니다.
    secondaryVariant = Secondary90, // 보조 변형 색상은 의 두 가지 요소를 구별하는 데 사용됩니다.
    onSecondary = White, // 보조 색상 위에 표시되는 텍스트 및 아이콘에 사용되는 색상입니다.
    background = Neutral99, // 스크롤 가능한 콘텐츠 뒤에 배경색이 나타납니다.
    onBackground = Neutral10, // 배경색 위에 표시되는 텍스트 및 아이콘에 사용되는 색상입니다.
    surface = Neutral99, // 표면 색상은 카드, 시트 및 같은 구성 요소의 표면에 사용됩니다.
    onSurface = Neutral10, // 표면 색상 위에 표시되는 텍스트 및 아이콘에 사용됩니다.
    error = Error40, // 오류 색상은 텍스트 필드와 같은 구성 요소 내의 오류를 나타내는 데 사용됩니다.
    onError = White, // 오류 색상 위에 표시되는 텍스트 및 아이콘에 사용됩니다.
)

private val DarkColorPalette = darkColors(
    primary = Primary80, // 기본 색상은 앱에서 가장 자주 표시되는 색상입니다.
    primaryVariant = Primary30, // 기본 변형 색상은 두 요소를 구별하는 데 사용됩니다.
    onPrimary = Primary20, // 주색 위에 표시되는 텍스트 및 아이콘에 사용되는 색상입니다.
    secondary = Secondary80, // 두 번째 색상은 당신을 강조하고 구별할 수 있는 더 많은 방법을 제공합니다.
    secondaryVariant = Secondary30, // 보조 변형 색상은 의 두 가지 요소를 구별하는 데 사용됩니다.
    onSecondary = Secondary20, // 보조 색상 위에 표시되는 텍스트 및 아이콘에 사용되는 색상입니다.
    background = Neutral10, // 스크롤 가능한 콘텐츠 뒤에 배경색이 나타납니다.
    onBackground = Neutral90, // 배경색 위에 표시되는 텍스트 및 아이콘에 사용되는 색상입니다.
    surface = Neutral10, // 표면 색상은 카드, 시트 및 같은 구성 요소의 표면에 사용됩니다.
    onSurface = Neutral90, // 표면 색상 위에 표시되는 텍스트 및 아이콘에 사용됩니다.
    error = Error80, // 오류 색상은 텍스트 필드와 같은 구성 요소 내의 오류를 나타내는 데 사용됩니다.
    onError = Error20, // 오류 색상 위에 표시되는 텍스트 및 아이콘에 사용됩니다.
)

@Composable
fun MagoHzTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = PretendardTypography,
        content = content
    )
}