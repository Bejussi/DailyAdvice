package com.bejussi.dailyadvice.presentation.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Insets.add
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bejussi.dailyadvice.di.dataStore
import com.bejussi.dailyadvice.domain.AdviceRepository
import com.bejussi.dailyadvice.domain.SettingsDataStoreRepository
import dagger.hilt.EntryPoint
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

object AlarmScheduler {

    const val REMINDER_NOTIFICATION_REQUEST_CODE = 123

    fun schedule(
        context: Context,
        time: String = "08:00",
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext,
                reminderId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val (hours, min) = time.split(":").map { it.toInt() }
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, min)
        }

        if (Calendar.getInstance(Locale.ENGLISH)
                .apply { add(Calendar.MINUTE, 1) }.timeInMillis - calendar.timeInMillis > 0
        ) {
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(
                calendar.timeInMillis,
                intent
            ),
            intent
        )
    }
}