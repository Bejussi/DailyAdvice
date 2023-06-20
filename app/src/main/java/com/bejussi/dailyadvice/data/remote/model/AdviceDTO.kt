package com.bejussi.dailyadvice.data.remote.model

import com.bejussi.dailyadvice.domain.model.Advice

data class AdviceDTO(
    val slip: SlipDTO
){
    fun toDomain(): Advice = Advice(
        slip = slip.toDomain()
    )
}