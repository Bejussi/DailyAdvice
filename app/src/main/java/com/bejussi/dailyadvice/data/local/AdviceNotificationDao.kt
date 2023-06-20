package com.bejussi.dailyadvice.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AdviceNotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdviceNotification(adviceNotification: AdviceNotificationData)

    @Query("SELECT * FROM adviceNotification")
    suspend fun getAdviceNotification(): AdviceNotificationData

    @Query("DELETE FROM adviceNotification")
    suspend fun deleteAdviceNotification()
}