package com.andersen.feature_rooms_screen.presentation.new_event.dialog_time_for_notifications

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import com.andersen.feature_rooms_screen.domain.entity.NotificationData
import com.meetingroom.andersen.feature_rooms_screen.R

class NotificationHelper(private val context: Context) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                REMINDER_NOTIFICATION_CHANNEL,
                context.getString(R.string.notification_channel_reminder_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                description =
                    context.getString(R.string.notification_channel_reminder_description)
                val notificationManager =
                    context.getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(this)
            }
        }
    }

    companion object {
        const val REMINDER_NOTIFICATION_DESCRIPTION = "description"
        const val REMINDER_NOTIFICATION_TITLE = "title"
        const val REMINDER_NOTIFICATION_CHANNEL = "REMINDER_NOTIFICATION_CHANNEL"
        const val UPCOMING_EVENT_NOTIFICATION_ID = 1

        @SuppressLint("UnspecifiedImmutableFlag")
        fun setNotification(
            notificationData: NotificationData,
            notificationHelper: NotificationHelper,
            reminderStartTime: Long,
        ) {
            val notificationDescription =
                String.format(
                    notificationHelper.context.resources.getString(R.string.event_upcoming_notification_description),
                    notificationData.roomTitle,
                    notificationData.startTime,
                    notificationData.remainingTime,
                )
            val intent =
                Intent(notificationHelper.context, ReceiverForUpcomingEvent::class.java).apply {
                    putExtra(REMINDER_NOTIFICATION_TITLE, notificationData.title)
                    putExtra(REMINDER_NOTIFICATION_DESCRIPTION, notificationDescription)
                }
            val pendingIntent = PendingIntent.getBroadcast(
                notificationHelper.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager =
                notificationHelper.context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            alarmManager?.set(
                AlarmManager.RTC_WAKEUP,
                reminderStartTime,
                pendingIntent
            )
        }
    }
}