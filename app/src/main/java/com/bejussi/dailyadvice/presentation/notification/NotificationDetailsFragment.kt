package com.bejussi.dailyadvice.presentation.notification

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.core.util.makeToast
import com.bejussi.dailyadvice.databinding.FragmentNotificationDetailsBinding
import com.bejussi.dailyadvice.presentation.advice.SaveToStorageManager
import com.bejussi.dailyadvice.presentation.core.UIEvent
import com.bejussi.dailyadvice.presentation.settings.SettingsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationDetailsFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentNotificationDetailsBinding

    private val args: NotificationDetailsFragmentArgs by navArgs()
    private val notifiicationAdviceViewModel: NotifiicationAdviceViewModel by viewModels()

    @Inject
    lateinit var saveToStorageManager: SaveToStorageManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNotificationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.notificationAdviceId

        notifiicationAdviceViewModel.getAdviceById(id = id)

        lifecycleScope.launch {
            notifiicationAdviceViewModel.eventFlow.collect { event ->
                when (event) {
                    is UIEvent.ShowToast -> requireContext().makeToast(
                        message = event.message
                    )
                }
            }
        }

        lifecycleScope.launch {
            notifiicationAdviceViewModel.state.collect { state ->
                binding.apply {
                    progress.isVisible = state.isLoading
                    adviceId.text = getString(R.string.advice_id, state.advice?.slip?.id)
                    adviceText.text = state.advice?.slip?.advice
                }
            }
        }

        binding.shareAdviceButton.setOnClickListener(this)
        binding.backButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.shareAdviceButton -> shareAdvice()
            R.id.backButton -> navigate()
        }
    }

    private fun shareAdvice() {
        changeVisible(true)
        val bitmap = binding.layout.drawToBitmap()
        val uri = saveToStorageManager.saveBitmap(bitmap)
            ?: requireContext().makeToast("Save image error")
        if (uri is Uri) shareImage(uri)
        changeVisible(false)
    }

    private fun shareImage(file: Uri) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, file)
            type = "image/jpeg"
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }

    private fun navigate() {
        val action = NotificationDetailsFragmentDirections.actionNotificationDetailsFragmentToAdviceFragment()
        findNavController().navigate(action)
    }

    private fun changeVisible(isTakeScreenshot: Boolean) {
        val viewsToHide = listOf(
            R.id.shareAdviceButton,
            R.id.backButton
        )

        val visibility = if (isTakeScreenshot) View.INVISIBLE else View.VISIBLE

        binding.layout.children.iterator().forEachRemaining { view ->
            if (viewsToHide.contains(view.id)) {
                view.visibility = visibility
            }
        }
    }
}