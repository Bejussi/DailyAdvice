package com.bejussi.dailyadvice.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AdviceNotificationData::class],
    version = 1,
    exportSchema = false
)
abstract class AdviceNotificationDatabase: RoomDatabase() {
    abstract fun adviceNotificationDao(): AdviceNotificationDao

    companion object {
        const val DATABASE_NAME = "advice_notification_database"
    }
}