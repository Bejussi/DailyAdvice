package com.bejussi.dailyadvice.data.local.mapper

import com.bejussi.dailyadvice.core.util.Mapper
import com.bejussi.dailyadvice.data.local.AdviceNotificationData
import com.bejussi.dailyadvice.domain.model.AdviceNotification
import javax.inject.Inject

class MapperToDomain @Inject constructor(): Mapper<AdviceNotificationData, AdviceNotification> {
    override suspend fun map(from: AdviceNotificationData): AdviceNotification {
        return AdviceNotification(
            id = from.id,
            advice = from.advice
        )
    }
}