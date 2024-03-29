package com.bejussi.dailyadvice.domain

import com.bejussi.dailyadvice.core.util.Resource
import com.bejussi.dailyadvice.domain.model.Advice
import com.bejussi.dailyadvice.domain.model.AdviceNotification
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AdviceRepository {

    fun getRandomAdvice(): Flow<Resource<Advice>>

    fun getAdviceById(id: Int): Flow<Resource<Advice>>

    suspend fun getRandomAdviceNotification()

    suspend fun getNotificationAdvice(): AdviceNotification

    suspend fun deleteNotificationAdvice()

    suspend fun insertNotificationAdvice(adviceNotification: AdviceNotification)
}