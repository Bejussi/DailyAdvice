package com.bejussi.dailyadvice.presentation.advice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.core.util.makeToast
import com.bejussi.dailyadvice.databinding.FragmentAdviceBinding
import com.bejussi.dailyadvice.presentation.core.UIEvent
import com.bejussi.dailyadvice.presentation.notification.NotificationDetailsFragmentArgs
import com.bejussi.dailyadvice.presentation.notification.alarm.AlarmReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AdviceFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentAdviceBinding
    private val adviceViewModel: AdviceViewModel by viewModels()

    @Inject
    lateinit var saveToStorageManager: SaveToStorageManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAdviceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            adviceViewModel.eventFlow.collect { event ->
                when (event) {
                    is UIEvent.ShowToast -> requireContext().makeToast(
                        message = event.message
                    )
                }
            }
        }

        lifecycleScope.launch {
            adviceViewModel.state.collect { state ->
                binding.apply {
                    adviceLayout.isVisible = !state.isError
                    refreshText.isVisible = state.isError
                    swipeRefreshLayout.isRefreshing = state.isLoading
                    adviceId.text = getString(R.string.advice_id, state.advice?.slip?.id)
                    adviceText.text = state.advice?.slip?.advice
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                adviceViewModel.getRandomAdvice()
            }
        }

        binding.shareAdviceButton.setOnClickListener(this)
        binding.settingsButton.setOnClickListener(this)

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.shareAdviceButton -> shareAdvice()
            R.id.settings_button -> navigateToSettings()
        }
    }

    private fun navigateToSettings() {
        val action = AdviceFragmentDirections.actionAdviceFragmentToSettingsFragment()
        findNavController().navigate(action)
    }

    private fun shareAdvice() {
        changeVisible(true)
        val bitmap = binding.layout.drawToBitmap()
        val uri = saveToStorageManager.saveBitmap(bitmap)
            ?: requireContext().makeToast("Save image error")
        if (uri is Uri) shareImage(uri)
        changeVisible(false)
    }

    private fun changeVisible(isTakeScreenshot: Boolean) {
        val viewsToHide = listOf(
            R.id.shareAdviceButton,
            R.id.appName,
            R.id.premuim_button,
            R.id.settings_button
        )

        val visibility = if (isTakeScreenshot) View.INVISIBLE else View.VISIBLE

        binding.layout.children.iterator().forEachRemaining { view ->
            if (viewsToHide.contains(view.id)) {
                view.visibility = visibility
            }
        }
    }

    private fun shareImage(file: Uri) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, file)
            type = "image/jpeg"
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }
}