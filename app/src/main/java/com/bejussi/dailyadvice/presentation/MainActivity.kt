package com.bejussi.dailyadvice.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.presentation.notification.AlarmScheduler
import com.bejussi.dailyadvice.presentation.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingsViewModel.getNotificationTime.observe(this) { time ->
            setAlarm(time)
        }
    }

    private fun setAlarm(time: String) {
        AlarmScheduler(this).schedule(time)
    }
}