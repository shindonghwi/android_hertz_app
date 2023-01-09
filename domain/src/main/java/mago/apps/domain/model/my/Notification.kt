package mago.apps.domain.model.my

data class Notification(
    val notificationSeq: Int,
    val title: String,
    val message: String,
    val link: String,
    val timeAgo: String
)
