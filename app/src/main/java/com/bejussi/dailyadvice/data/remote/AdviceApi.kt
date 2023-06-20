package com.bejussi.dailyadvice.data.remote

import com.bejussi.dailyadvice.data.remote.model.AdviceDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface AdviceApi {

    @GET("/advice")
    suspend fun gerRandomAdvice(): AdviceDTO

    @GET("/advice/{slip_id}")
    suspend fun gerAdviceById(@Path("slip_id") id: Int): AdviceDTO

}