package com.bejussi.dailyadvice.presentation.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AlarmScheduler(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(time: String) {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            getTime(time),
            PendingIntent.getBroadcast(
                context.applicationContext,
                time.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun getTime(time: String): Long {
        val (hours, min) = time.split(":").map { it.toInt() }
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, min)
        }
        return calendar.timeInMillis
    }
}