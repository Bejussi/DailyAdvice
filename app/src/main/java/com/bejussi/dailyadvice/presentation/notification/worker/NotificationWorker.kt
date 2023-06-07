package com.bejussi.dailyadvice.presentation.notification.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bejussi.dailyadvice.core.util.Resource
import com.bejussi.dailyadvice.domain.AdviceRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted parameters: WorkerParameters,
    private val adviceRepository: AdviceRepository,
    private val mapperSlipToAdviceNotification: MapperSlipToAdviceNotification
): CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        try {
            adviceRepository.deleteNotificationAdvice()
            adviceRepository.getRandomAdvice().collect {
                if (it is Resource.Success) {
                    adviceRepository.insertNotificationAdvice(mapperSlipToAdviceNotification.map(it.data!!.slip))
                }
            }
            return Result.success()
        } catch (throwable: Throwable) {
            return Result.retry()
        }
    }
}