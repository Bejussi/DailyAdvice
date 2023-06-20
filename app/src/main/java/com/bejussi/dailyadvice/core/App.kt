package com.bejussi.dailyadvice.core

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import com.bejussi.dailyadvice.presentation.notification.worker.NotificationWorker
import com.bejussi.dailyadvice.presentation.notification.worker.NotificationWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App: Application(), Configuration.Provider {

    @Inject
    lateinit var notificationWorkerFactory: NotificationWorkerFactory

    override fun onCreate() {
        super.onCreate()

        WorkManager.initialize(
            this,
            Configuration.Builder()
            .setWorkerFactory(notificationWorkerFactory)
            .build())

        createNotificationChannel()
        setupNotificationWorker()
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            "channel_id",
            "Notification Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun setupNotificationWorker() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myWork = PeriodicWorkRequestBuilder<NotificationWorker>(
            3,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "MyUniqueWorkName",
            ExistingPeriodicWorkPolicy.KEEP,
            myWork
        )
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(notificationWorkerFactory)
            .build()
}