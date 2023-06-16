package com.bejussi.dailyadvice.presentation.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.ProductDetails
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.core.util.makeToast
import com.bejussi.dailyadvice.databinding.FragmentSettingsBinding
import com.bejussi.dailyadvice.presentation.settings.billing.BillingActionListener
import com.bejussi.dailyadvice.presentation.settings.billing.BillingAdapter
import com.bejussi.dailyadvice.presentation.settings.billing.BillingViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val billingViewModel: BillingViewModel by viewModels()

    private lateinit var adapter: BillingAdapter

    private var selectedThemeIndex = 0
    private var selectedLanguageIndex = 0
    private var selectedTime = "12:00"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        billingViewModel.productsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        billingViewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progress.isVisible = loading
            binding.donateChoose.isVisible = !loading
        }

        binding.backButton.setOnClickListener(this)
        binding.remindersButton.setOnClickListener(this)
        binding.languageButton.setOnClickListener(this)
        binding.themeButton.setOnClickListener(this)
        binding.rateUsButton.setOnClickListener(this)
        binding.contactButton.setOnClickListener(this)
        binding.privacyPolicyButton.setOnClickListener(this)
        binding.termsConditionsButton.setOnClickListener(this)

        settingsViewModel.getTheme.observe(viewLifecycleOwner) { themeMode ->
            when (themeMode) {
                "light" -> {
                    selectedThemeIndex = 0
                }
                "dark" -> {
                    selectedThemeIndex = 1
                }
                "system" -> {
                    selectedThemeIndex = 2
                }
            }
        }

        settingsViewModel.getLanguage.observe(viewLifecycleOwner) { language ->
            when (language) {
                "en" -> {
                    selectedLanguageIndex = 0
                }
                "uk" -> {
                    selectedLanguageIndex = 1
                }
                "ru" -> {
                    selectedLanguageIndex = 2
                }
                "de" -> {
                    selectedLanguageIndex = 3
                }
                "fr" -> {
                    selectedLanguageIndex = 4
                }
                "es" -> {
                    selectedLanguageIndex = 5
                }
            }
        }

        settingsViewModel.getNotificationTime.observe(viewLifecycleOwner) { time ->
            selectedTime = time
        }
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.backButton -> navigate()
            R.id.remindersButton -> setReminder()
            R.id.languageButton -> setLanguage()
            R.id.themeButton -> setTheme()
            R.id.rateUsButton -> rateUs()
            R.id.contactButton -> contactUs()
            R.id.privacyPolicyButton -> showPrivacy()
            R.id.termsConditionsButton -> showTerms()
        }
    }

    private fun setupRecyclerView() {
        adapter = BillingAdapter(object : BillingActionListener {
            override fun startBilling(productDetails: ProductDetails) {
                billingViewModel.launchBillingFlow(requireActivity(), productDetails)
            }
        })
        binding.donateChoose.adapter = adapter
    }

    private fun showTerms() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToWebFragment("terms")
        findNavController().navigate(action)
    }

    private fun showPrivacy() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToWebFragment("privacy")
        findNavController().navigate(action)
    }

    private fun contactUs() {
        val intent = Intent(Intent.ACTION_SENDTO)
            .setData(Uri.parse("mailto:?subject=DailyAdvice&body=Feedback for DailyAdvice&to=bejussiapp@gmail.com"))
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            requireContext().makeToast(getString(R.string.application_not_found))
        }
    }

    private fun rateUs() {
        val appPackageName = requireActivity().packageName
        try {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            )
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
            startActivity(intent)
        }
    }

    private fun setTheme() {
        val themes = resources.getStringArray(R.array.themes)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.select_theme))
            .setSingleChoiceItems(themes, selectedThemeIndex) { dialog_, which ->
                selectedThemeIndex = which
            }
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                setTheme(selectedThemeIndex)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setTheme(selectedThemeIndex: Int) {
        lifecycleScope.launch {
            when (selectedThemeIndex) {
                0 -> {
                    settingsViewModel.setTheme("light")
                }
                1 -> {
                    settingsViewModel.setTheme("dark")
                }
                2 -> {
                    settingsViewModel.setTheme("system")
                }
            }
        }
    }

    private fun setLanguage() {
        val languages = resources.getStringArray(R.array.language)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.select_language))
            .setSingleChoiceItems(languages, selectedLanguageIndex) { dialog_, which ->
                selectedLanguageIndex = which
            }
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                setLanguage(selectedLanguageIndex)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setLanguage(selectedLanguageIndex: Int) {
        lifecycleScope.launch {
            when (selectedLanguageIndex) {
                0 -> {
                    settingsViewModel.setLanguage("en")
                }
                1 -> {
                    settingsViewModel.setLanguage("uk")
                }
                2 -> {
                    settingsViewModel.setLanguage("ru")
                }
                3 -> {
                    settingsViewModel.setLanguage("de")
                }
                4 -> {
                    settingsViewModel.setLanguage("fr")
                }
                5 -> {
                    settingsViewModel.setLanguage("es")
                }
            }
        }
    }

    private fun setReminder() {
        val (hours, min) = selectedTime.split(":").map { it.toInt() }

        val materialTimePicker = MaterialTimePicker.Builder()
            .setTitleText(getString(R.string.select_time))
            .setHour(hours)
            .setMinute(min)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

        materialTimePicker.show(requireActivity().supportFragmentManager, "TIME_PICKER")

        materialTimePicker.addOnPositiveButtonClickListener {
            val pickedMinute: Int = materialTimePicker.minute

            val formattedTime = if (pickedMinute < 10) {
                "${materialTimePicker.hour}:0${materialTimePicker.minute}"
            } else {
                "${materialTimePicker.hour}:${materialTimePicker.minute}"
            }

            settingsViewModel.setNotificationTime(formattedTime)
        }

    }

    private fun navigate() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToAdviceFragment()
        findNavController().navigate(action)
    }
}