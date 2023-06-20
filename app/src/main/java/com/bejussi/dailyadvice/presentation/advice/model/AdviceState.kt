package com.bejussi.dailyadvice.presentation.advice.model

import com.bejussi.dailyadvice.domain.model.Advice

data class AdviceState(
    val advice: Advice? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false
)