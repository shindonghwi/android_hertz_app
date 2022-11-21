package mago.apps.hertz.ui.screens.components.appbar

enum class AppbarType {
    EMPTY, // 없는 경우
    PROFILE_TITLE_LEFT, // 프로필 이미지 + 타이틀 왼쪽 배치
    ONLY_TITLE_CENTER, // 프로필 이미지 + 타이틀 왼쪽 배치
    ICON_TITLE_ICON, // Composable + 타이틀 중앙 배치 + Composable
}