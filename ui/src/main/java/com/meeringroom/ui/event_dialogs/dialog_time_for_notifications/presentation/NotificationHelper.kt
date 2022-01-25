package com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.model.NotificationData
import com.meetingroom.ui.R

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
        lateinit var resultIntent: PendingIntent

        @SuppressLint("UnspecifiedImmutableFlag")
        fun setNotification(
            notificationData: NotificationData,
            notificationHelper: NotificationHelper,
            reminderStartTime: Long,
            activity: Activity
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

            resultIntent = getResultIntent(activity, notificationHelper)
        }

        private fun getResultIntent(
            activity: Activity,
            notificationHelper: NotificationHelper
        ): PendingIntent {
            val resultIntent = Intent(notificationHelper.context, activity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            return PendingIntent.getActivity(
                notificationHelper.context,
                0,
                resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
    }
}
