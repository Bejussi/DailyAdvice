package com.bejussi.dailyadvice.presentation.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentSettingsBinding

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

        binding.backButton.setOnClickListener(this)
        binding.subscriptionButton.setOnClickListener(this)
        binding.remindersButton.setOnClickListener(this)
        binding.languageButton.setOnClickListener(this)
        binding.themeButton.setOnClickListener(this)
        binding.rateUsButton.setOnClickListener(this)
        binding.contactButton.setOnClickListener(this)
        binding.privacyPolicyButton.setOnClickListener(this)
        binding.termsConditionsButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.backButton -> navigate()
            R.id.subscriptionButton -> manageSubscription()
            R.id.remindersButton -> setReminder()
            R.id.languageButton -> setLanguage()
            R.id.themeButton -> setTheme()
            R.id.rateUsButton -> rateUs()
            R.id.contactButton -> contactUs()
            R.id.privacyPolicyButton -> showPrivacy()
            R.id.termsConditionsButton -> showTerms()
        }
    }

    private fun showTerms() {
        TODO("Not yet implemented")
    }

    private fun showPrivacy() {
        TODO("Not yet implemented")
    }

    private fun contactUs() {
        TODO("Not yet implemented")
    }

    private fun rateUs() {
        TODO("Not yet implemented")
    }

    private fun setTheme() {
        TODO("Not yet implemented")
    }

    private fun setLanguage() {
        TODO("Not yet implemented")
    }

    private fun setReminder() {
        TODO("Not yet implemented")
    }

    private fun manageSubscription() {
        TODO("Not yet implemented")
    }

    private fun navigate() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToAdviceFragment()
        findNavController().navigate(action)
    }
}