package com.bejussi.dailyadvice.presentation.notification.alarm

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.domain.AdviceRepository
import com.bejussi.dailyadvice.domain.SettingsDataStoreRepository
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

    @Inject
    lateinit var settingsDataStoreRepository: SettingsDataStoreRepository
    lateinit var time: String

    override fun onReceive(context: Context, intent: Intent?) {

        CoroutineScope(IO).launch {
            val advice = adviceRepository.getNotificationAdvice()
            settingsDataStoreRepository.getNotificationTime().collect { time ->
                setNotification(advice, context, time)
            }
        }
    }

    private fun setNotification(advice: AdviceNotification, context: Context, time: String) {

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val bundle = Bundle()
        bundle.putInt(NOTIFICATION_ADVICE_ID, advice.id)

        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.notificationDetailsFragment)
            .setArguments(bundle)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.title_notification_reminder))
            .setContentText(advice.advice)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(advice.advice))
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

        AlarmScheduler.schedule(context.applicationContext, time)
    }

    companion object {
        const val NOTIFICATION_ADVICE_ID = "notification_advice_id"
        const val CHANNEL_ID = "channel_id"
        const val NOTIFY_ID = 10
    }
}