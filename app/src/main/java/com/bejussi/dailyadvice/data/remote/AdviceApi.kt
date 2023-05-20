package com.bejussi.dailyadvice.data.remote

import com.bejussi.dailyadvice.data.remote.model.AdviceDTO
import retrofit2.http.GET

interface AdviceApi {

    @GET("/advice")
    suspend fun gerRandomAdvice(): AdviceDTO
}