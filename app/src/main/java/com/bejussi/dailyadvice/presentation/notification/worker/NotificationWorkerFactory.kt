package com.bejussi.dailyadvice.presentation.notification.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.bejussi.dailyadvice.domain.AdviceRepository
import javax.inject.Inject

class NotificationWorkerFactory @Inject constructor (
    private val adviceRepository: AdviceRepository
    ): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return NotificationWorker(
            appContext,
            workerParameters,
            adviceRepository
        )
    }

}