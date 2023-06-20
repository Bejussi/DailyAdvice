package com.bejussi.dailyadvice.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bejussi.dailyadvice.domain.SettingsDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStoreRepository: SettingsDataStoreRepository
): ViewModel() {

    val getTheme = settingsDataStoreRepository.getTheme().asLiveData(Dispatchers.IO)
    val getLanguage = settingsDataStoreRepository.getLanguage().asLiveData(Dispatchers.IO)
    val getNotificationTime = settingsDataStoreRepository.getNotificationTime().asLiveData(Dispatchers.IO)

    fun setTheme(themeMode : String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsDataStoreRepository.setTheme(themeMode)
        }
    }

    fun setLanguage(language : String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsDataStoreRepository.setLanguage(language)
        }
    }

    fun setNotificationTime(notificationTime : String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsDataStoreRepository.setNotificationTime(notificationTime)
        }
    }
}