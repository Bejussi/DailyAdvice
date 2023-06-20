package com.bejussi.dailyadvice.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.presentation.notification.alarm.AlarmScheduler
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

        settingsViewModel.getTheme.observe(this@MainActivity) { themeMode ->
            when (themeMode) {
                "light" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                "dark" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                "system" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }

        settingsViewModel.getLanguage.observe(this@MainActivity) { language ->
            val localeList = LocaleListCompat.forLanguageTags(language)
            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }

    private fun setAlarm(time: String) {
        AlarmScheduler.schedule(this, time)
    }
}