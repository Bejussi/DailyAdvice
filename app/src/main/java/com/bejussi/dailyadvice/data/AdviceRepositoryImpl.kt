package com.bejussi.dailyadvice.data

import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.core.util.Resource
import com.bejussi.dailyadvice.core.util.StringResourcesProvider
import com.bejussi.dailyadvice.data.local.AdviceNotificationDao
import com.bejussi.dailyadvice.data.local.AdviceNotificationData
import com.bejussi.dailyadvice.data.local.mapper.MapperToData
import com.bejussi.dailyadvice.data.local.mapper.MapperToDomain
import com.bejussi.dailyadvice.data.remote.AdviceApi
import com.bejussi.dailyadvice.domain.AdviceRepository
import com.bejussi.dailyadvice.domain.model.Advice
import com.bejussi.dailyadvice.domain.model.AdviceNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AdviceRepositoryImpl @Inject constructor(
    private val adviceApi: AdviceApi,
    private val stringResourcesProvider: StringResourcesProvider,
    private val adviceNotificationDao: AdviceNotificationDao,
    private val mapperToData: MapperToData,
    private val mapperToDomain: MapperToDomain
): AdviceRepository {

    override fun getRandomAdvice(): Flow<Resource<Advice>> = flow {
        emit(Resource.Loading())

        try {
            val data = adviceApi.gerRandomAdvice().toDomain()
            emit(Resource.Success(data = data))
        } catch (e: HttpException) {
            emit(Resource.Error(
                message = stringResourcesProvider.getString(R.string.error),
                data = null
            ))
        } catch (e: IOException) {
            emit(Resource.Error(
                message = stringResourcesProvider.getString(R.string.checkInternet),
                data = null
            ))
        }
    }

    override suspend fun getNotificationAdvice(): AdviceNotification {
        val adviceNotification = adviceNotificationDao.getAdviceNotification()
        return mapperToDomain.map(adviceNotification)
    }

    override suspend fun deleteNotificationAdvice() {
        adviceNotificationDao.deleteAdviceNotification()
    }

    override suspend fun insertNotificationAdvice(adviceNotification: AdviceNotification) {
        adviceNotificationDao.insertAdviceNotification(mapperToData.map(adviceNotification))
    }
}