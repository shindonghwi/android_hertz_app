package mago.apps.hertz.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import mago.apps.hertz.MainActivity
import mago.apps.hertz.R
import mago.apps.hertz.firebase.NotificationType.COMMON_GROUP
import mago.apps.hertz.firebase.NotificationType.NOTI_CHANNEL_DESCRIPTION
import mago.apps.hertz.firebase.NotificationType.NOTI_CHANNEL_ID
import mago.apps.hertz.firebase.NotificationType.NOTI_CHANNEL_NAME
import mago.apps.hertz.firebase.NotificationType.NOTI_CODE
import mago.apps.hertz.firebase.NotificationType.NOTI_GROUP_CODE
import mago.apps.hertz.firebase.NotificationType.NOTI_GROUP_NAME

class NotificationHelper(private val context: Context) {

    fun pushNotificationGroup(
        id: String,
        title: String,
        body: String,
        imageUrl: String,
        linkUrl: String
    ) {
        val newNotification = createNotification(title, body, linkUrl).build()
        val groupNotification = createSummaryNotification(title, body, linkUrl).build()

        val notificationManager =
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                setupNotificationChannels()
            }
        val channel = NotificationChannel(
            NOTI_CHANNEL_ID,
            NOTI_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = NOTI_CHANNEL_DESCRIPTION
        notificationManager.createNotificationChannel(channel)

        NotificationManagerCompat.from(context).apply {
            notify(id.hashCode(), newNotification)
            notify(COMMON_GROUP, groupNotification)
        }
    }

    private fun NotificationManager.setupNotificationChannels() {
        val adminChannel =
            NotificationChannel(
                NOTI_CHANNEL_ID,
                NOTI_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = NOTI_CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }
        createNotificationChannel(adminChannel)
    }

    private fun createNotification(
        title: String,
        body: String,
        linkUrl: String
    ): NotificationCompat.Builder {
        val pendingIntent = PendingIntent.getActivity(
            context, NOTI_CODE.hashCode(), Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra("linkUrl", linkUrl)
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
            }, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, NOTI_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle(title)
            .setContentText(body).setGroup(NOTI_GROUP_NAME).setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }

    private fun createSummaryNotification(
        title: String,
        body: String,
        linkUrl: String
    ): NotificationCompat.Builder {

        val pendingIntent = PendingIntent.getActivity(
            context, NOTI_GROUP_CODE.hashCode(), Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra("linkUrl", linkUrl)
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
            }, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        return NotificationCompat.Builder(context, NOTI_CHANNEL_ID)
            .setSmallIcon(coil.base.R.drawable.notification_bg).setContentTitle(title)
            .setContentText(body).setChannelId(NOTI_CHANNEL_ID).setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true).setAutoCancel(true)
            .setGroup(NOTI_GROUP_NAME).setGroupSummary(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

    }

}