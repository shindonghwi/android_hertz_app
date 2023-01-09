package mago.apps.data.network.model.my

data class NotificationDTO(
    val notificationSeq: Int,
    val title: String,
    val message: String,
    val link: String,
    val timeAgo: String
)
