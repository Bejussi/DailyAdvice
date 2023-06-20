package com.bejussi.dailyadvice.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bejussi.dailyadvice.domain.model.AdviceNotification

@Entity(tableName = "adviceNotification")
data class AdviceNotificationData(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val advice: String
)
