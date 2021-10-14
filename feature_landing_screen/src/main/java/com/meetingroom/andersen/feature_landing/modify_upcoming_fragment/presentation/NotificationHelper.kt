package com.meetingroom.andersen.feature_landing.modify_upcoming_fragment.presentation

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData
import javax.inject.Inject

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
            upcomingEventData: UpcomingEventData,
            notificationHelper: NotificationHelper,
            reminderStartTime: Long,
        ) {
            val notificationDescription =
                String.format(
                    notificationHelper.context.resources.getString(R.string.event_upcoming_notification_description),
                    upcomingEventData.eventRoom,
                    upcomingEventData.startTime,
                    upcomingEventData.reminderRemainingTime,
                )
            val intent =
                Intent(notificationHelper.context, ReceiverForUpcomingEvent::class.java).apply {
                    putExtra(REMINDER_NOTIFICATION_TITLE, upcomingEventData.title)
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