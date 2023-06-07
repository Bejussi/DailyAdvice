package com.bejussi.dailyadvice.core

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.work.Configuration
import com.bejussi.dailyadvice.presentation.notification.worker.WorkHandler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application(), Configuration.Provider {

    init {
        appInstance = this
    }
    override fun onCreate() {
        super.onCreate()
        val notificationChannel = NotificationChannel(
            "channel_id",
            "Notification Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)

        WorkHandler.createWork()
    }

    companion object {
        lateinit var appInstance: App
    }

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setMinimumLoggingLevel(android.util.Log.DEBUG)
        .build()
}