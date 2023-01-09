package mago.apps.data.mappers.my

import mago.apps.data.network.model.my.NotificationDTO
import mago.apps.domain.model.my.Notification

fun NotificationDTO.toDomain(): Notification {
    return Notification(notificationSeq, title, message, link, timeAgo)
}