package com.bejussi.dailyadvice.data.remote.model

import com.bejussi.dailyadvice.domain.model.Slip

data class SlipDTO(
    val advice: String,
    val id: Int
){
    fun toDomain(): Slip = Slip(
        advice = advice,
        id = id
    )
}