package com.bejussi.dailyadvice.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.bejussi.dailyadvice.domain.SettingsDataStoreRepository
import com.bejussi.dailyadvice.domain.model.Advice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SettingsDataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): SettingsDataStoreRepository {

    override suspend fun setTheme(themeMode: String) {
        setData(themeModeKey, themeMode)
    }

    override suspend fun setLanguage(language: String) {
        setData(languageKey, language)
    }

    override suspend fun setNotificationTime(time: String) {
        setData(notificationKey, time)
    }

    override fun getTheme(): Flow<String> {
        return getData(themeModeKey, "light")
    }

    override fun getLanguage(): Flow<String> {
        return getData(languageKey, "en")
    }

    override fun getNotificationTime(): Flow<String> {
        return getData(notificationKey, "17:00")
    }

    fun getData(
        key: Preferences.Key<String>,
        default: String
    ): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val uiMode = preferences[key] ?: default
                uiMode
            }
    }

    suspend fun setData(
        key: Preferences.Key<String>,
        value: String
    ) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    companion object {
        val themeModeKey = stringPreferencesKey("THEME_MODE_KEY")
        val languageKey = stringPreferencesKey("LANGUAGE_KEY")
        val notificationKey = stringPreferencesKey("NOTIFICATION_KEY")
    }
}