package com.bejussi.dailyadvice.presentation.notification.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bejussi.dailyadvice.domain.SettingsDataStoreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver: BroadcastReceiver() {

    @Inject
    lateinit var settingsDataStoreRepository: SettingsDataStoreRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            CoroutineScope(Dispatchers.IO).launch {
               settingsDataStoreRepository.getNotificationTime().collect { time ->
                   AlarmScheduler.schedule(context, time)
                }
            }
        }
    }
}