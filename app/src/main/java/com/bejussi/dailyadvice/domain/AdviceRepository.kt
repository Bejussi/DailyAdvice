package com.bejussi.dailyadvice.domain

import com.bejussi.dailyadvice.core.util.Resource
import com.bejussi.dailyadvice.domain.model.Advice
import kotlinx.coroutines.flow.Flow

interface AdviceRepository {

    fun getRandomAdvice(): Flow<Resource<Advice>>
}