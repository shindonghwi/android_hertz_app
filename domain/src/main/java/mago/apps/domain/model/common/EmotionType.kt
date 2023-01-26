package mago.apps.domain.model.common

enum class EmotionType {
    HAPPINESS, // 행복
    SADNESS, // 슬픔
    ANGRY, // 화남
    NEUTRAL // 중립
}

val EmotionList = listOf(
    Pair("\uD83D\uDE04", EmotionType.HAPPINESS),
    Pair("\uD83D\uDE22", EmotionType.SADNESS),
    Pair("\uD83D\uDE21", EmotionType.ANGRY),
    Pair("\uD83D\uDE36", EmotionType.NEUTRAL),
)