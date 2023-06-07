package com.bejussi.dailyadvice.data.local.mapper

import com.bejussi.dailyadvice.core.util.Mapper
import com.bejussi.dailyadvice.data.local.AdviceNotificationData
import com.bejussi.dailyadvice.domain.model.AdviceNotification
import javax.inject.Inject

class MapperToData @Inject constructor(): Mapper<AdviceNotification, AdviceNotificationData> {
    override suspend fun map(from: AdviceNotification): AdviceNotificationData {
        return AdviceNotificationData(
            id = from.id,
            advice = from.advice
        )
    }
}