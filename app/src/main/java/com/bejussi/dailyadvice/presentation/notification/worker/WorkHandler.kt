package com.bejussi.dailyadvice.presentation.notification.worker

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bejussi.dailyadvice.core.App
import java.util.concurrent.TimeUnit

object WorkHandler {
    private val workManager = WorkManager.getInstance(App.appInstance)

    fun createWork() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myWork = PeriodicWorkRequestBuilder<NotificationWorker>(
            1,
            TimeUnit.HOURS
        ).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            "MyUniqueWorkName",
            ExistingPeriodicWorkPolicy.KEEP,
            myWork
        )
    }
}