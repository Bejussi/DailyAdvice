package com.bejussi.dailyadvice.presentation.notification.worker

import com.bejussi.dailyadvice.core.util.Mapper
import com.bejussi.dailyadvice.domain.model.AdviceNotification
import com.bejussi.dailyadvice.domain.model.Slip
import javax.inject.Inject

class MapperSlipToAdviceNotification @Inject constructor(): Mapper<Slip, AdviceNotification> {
    override suspend fun map(from: Slip): AdviceNotification {
        return AdviceNotification(
            id = from.id,
            advice = from.advice
        )
    }
}