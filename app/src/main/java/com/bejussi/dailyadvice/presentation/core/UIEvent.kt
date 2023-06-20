package com.bejussi.dailyadvice.presentation.core

sealed class UIEvent {
    data class ShowToast(val message: String): UIEvent()
}