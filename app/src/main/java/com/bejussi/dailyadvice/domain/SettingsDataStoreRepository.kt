package com.bejussi.dailyadvice.domain

import kotlinx.coroutines.flow.Flow

interface SettingsDataStoreRepository {

    suspend fun setTheme(themeMode: String)

    fun getTheme(): Flow<String>

    suspend fun setLanguage(language: String)

    fun getLanguage(): Flow<String>

    suspend fun setNotificationTime(time: String)

    fun getNotificationTime(): Flow<String>
}