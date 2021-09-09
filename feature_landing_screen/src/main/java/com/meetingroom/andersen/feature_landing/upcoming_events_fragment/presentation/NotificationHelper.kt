package com.meetingroom.andersen.feature_landing.upcoming_events_fragment.presentation

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.upcoming_events_fragment.model.UpcomingEventData
import kotlinx.datetime.Clock
import javax.inject.Inject

class NotificationHelper @Inject internal constructor(private val context: Context) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationReminderChannel = NotificationChannel(
                REMINDER_NOTIFICATION_CHANNEL,
                context.getString(R.string.notification_channel_reminder_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationReminderChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationReminderChannel.description =
                context.getString(R.string.notification_channel_reminder_description)
            val notificationManager =
                context.getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationReminderChannel)
        }
    }

    fun setNotification(upcomingEventData: UpcomingEventData) {
        scheduleNotification(upcomingEventData)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun scheduleNotification(upcomingEventData: UpcomingEventData) {
        val title = upcomingEventData.title
        val notificationDescription =
            String.format(
                context.resources.getString(R.string.event_upcoming_notification_description),
                upcomingEventData.eventRoom,
                upcomingEventData.startTime,
                upcomingEventData.reminderRemainingTime,
            )
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val notificationScheduledTime = 200 * 10
        val intent = Intent(context, BroadcastForUpcomingEvent::class.java)
        intent.putExtra(REMINDER_NOTIFICATION_TITLE, title)
        intent.putExtra(REMINDER_NOTIFICATION_DESCRIPTION, notificationDescription)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, 0
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.set(
            AlarmManager.RTC_WAKEUP,
            currentTime + notificationScheduledTime,
            pendingIntent
        )
    }

    companion object {
        const val REMINDER_NOTIFICATION_DESCRIPTION = "description"
        const val REMINDER_NOTIFICATION_TITLE = "title"
        const val REMINDER_NOTIFICATION_CHANNEL = "REMINDER_NOTIFICATION_CHANNEL"
        const val UPCOMING_EVENT_NOTIFICATION_ID = 1
    }
}