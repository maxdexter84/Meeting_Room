package com.meeringroom.ui.event_dialogs.dialog_time_for_notifications.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.meetingroom.ui.R

class ReceiverForUpcomingEvent : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        val notification = NotificationCompat.Builder(
            context,
            NotificationHelper.REMINDER_NOTIFICATION_CHANNEL
        ).setSmallIcon(R.drawable.ic_small_andersen_icon_notification)
            .setContentTitle(intent?.getStringExtra(NotificationHelper.REMINDER_NOTIFICATION_TITLE))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(intent?.getStringExtra(NotificationHelper.REMINDER_NOTIFICATION_DESCRIPTION))
            )
            .setAutoCancel(true)
            .setContentIntent(NotificationHelper.resultIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NotificationHelper.UPCOMING_EVENT_NOTIFICATION_ID, notification)
    }
}
