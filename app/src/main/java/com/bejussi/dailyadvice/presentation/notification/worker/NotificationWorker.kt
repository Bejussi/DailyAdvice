package com.bejussi.dailyadvice.presentation.notification.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bejussi.dailyadvice.core.util.Resource
import com.bejussi.dailyadvice.domain.AdviceRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted parameters: WorkerParameters,
    val adviceRepository: AdviceRepository
) : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        try {
            loadAdvice()
            Log.i("WORK_STATUS", "Worker started")
        } catch (e: HttpException) {
            Log.i("WORK_STATUS", "Worker error")
            return Result.retry()
        }

        return Result.success()
    }

    private suspend fun loadAdvice() {
        adviceRepository.apply {
            deleteNotificationAdvice()
            getRandomAdviceNotification()
        }
    }
}