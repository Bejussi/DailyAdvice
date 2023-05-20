package com.bejussi.dailyadvice.di

import com.bejussi.dailyadvice.data.AdviceRepositoryImpl
import com.bejussi.dailyadvice.domain.AdviceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideAdviceRepository(
        adviceRepositoryImpl: AdviceRepositoryImpl
    ): AdviceRepository = adviceRepositoryImpl
}