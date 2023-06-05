package com.bejussi.dailyadvice.domain

import com.bejussi.dailyadvice.core.util.Resource
import com.bejussi.dailyadvice.domain.model.Advice
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AdviceRepository {

    fun getRandomAdvice(): Flow<Resource<Advice>>
}