package com.bejussi.dailyadvice.presentation.notification

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.domain.AdviceRepository
import com.bejussi.dailyadvice.domain.model.Advice
import com.bejussi.dailyadvice.domain.model.AdviceNotification
import com.bejussi.dailyadvice.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    @Inject
    lateinit var adviceRepository: AdviceRepository

    override fun onReceive(context: Context, intent: Intent?) {

        CoroutineScope(IO).launch {
            val advice = adviceRepository.getNotificationAdvice()
            setNotification(advice, context)
        }
    }

    private fun setNotification(advice: AdviceNotification, context: Context) {

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            10,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.title_notification_reminder))
            .setContentText(advice.advice)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        val notificationManager = NotificationManagerCompat.from(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(NOTIFY_ID, notification.build())

        AlarmScheduler.schedule(context.applicationContext)
    }

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val NOTIFY_ID = 10
    }
}