package com.bejussi.dailyadvice.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bejussi.dailyadvice.data.AdviceRepositoryImpl
import com.bejussi.dailyadvice.data.SettingsDataStoreRepositoryImpl
import com.bejussi.dailyadvice.domain.AdviceRepository
import com.bejussi.dailyadvice.domain.SettingsDataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SETTINGS")

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideAdviceRepository(
        adviceRepositoryImpl: AdviceRepositoryImpl
    ): AdviceRepository = adviceRepositoryImpl


    @Provides
    @Singleton
    fun provideUserDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> {
        return applicationContext.dataStore
    }

    @Provides
    @Singleton
    fun provideSettingsDataStoreRepository(
        dataStore: DataStore<Preferences>
    ): SettingsDataStoreRepository = SettingsDataStoreRepositoryImpl(
        dataStore = dataStore
    )
}