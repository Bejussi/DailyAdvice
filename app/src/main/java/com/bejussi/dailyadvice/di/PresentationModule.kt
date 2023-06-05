package com.bejussi.dailyadvice.di

import android.content.ContentResolver
import android.content.Context
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.core.util.Resource
import com.bejussi.dailyadvice.core.util.StringResourcesProvider
import com.bejussi.dailyadvice.data.remote.AdviceApi
import com.bejussi.dailyadvice.domain.AdviceRepository
import com.bejussi.dailyadvice.domain.model.Advice
import com.bejussi.dailyadvice.domain.model.Slip
import com.bejussi.dailyadvice.presentation.advice.SaveToStorageManager
import com.bejussi.dailyadvice.presentation.core.UIEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    @Singleton
    fun provideContentResolver(
        @ApplicationContext context: Context
    ) = context.contentResolver

    @Provides
    @Singleton
    suspend fun provideAdvice(
        adviceApi: AdviceApi
    ): Advice {
        var advice: Advice
        coroutineScope {
            withContext(Dispatchers.IO) {
                advice = adviceApi.gerRandomAdvice().toDomain()
            }
        }
        return advice
    }
}