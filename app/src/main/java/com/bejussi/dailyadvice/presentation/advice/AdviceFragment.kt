package com.bejussi.dailyadvice.presentation.advice

import android.accessibilityservice.AccessibilityService.TakeScreenshotCallback
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.core.util.makeToast
import com.bejussi.dailyadvice.databinding.FragmentAdviceBinding
import com.bejussi.dailyadvice.presentation.core.UIEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.file.Path


@AndroidEntryPoint
class AdviceFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentAdviceBinding
    private val adviceViewModel: AdviceViewModel by viewModels()

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
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.shareAdviceButton -> shareAdvice()
        }
    }

    private fun shareAdvice() {
        changeVisible(true)
        val bitmap = binding.layout.drawToBitmap()
        saveBitmap(bitmap)
        changeVisible(false)
    }

    private fun changeVisible(isTakeScreenshot: Boolean) {
        binding.layout.children.iterator().forEachRemaining {
            if (isTakeScreenshot) {
                when (it.id) {
                    R.id.shareAdviceButton, R.id.appName, R.id.premuim_button, R.id.settings_button -> {
                        it.visibility = View.INVISIBLE
                    }
                }

            } else {
                when (it.id) {
                    R.id.shareAdviceButton, R.id.appName, R.id.premuim_button, R.id.settings_button -> {
                        it.visibility = View.VISIBLE
                    }
                }
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

    private fun saveBitmap(bitmap: Bitmap) {
        val timestamp = System.currentTimeMillis()
        var uri: Uri? = null
        var path: String? = null

        //Tell the media scanner about the new file so that it is immediately available to the user.
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            values.put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/" + getString(R.string.app_name)
            )
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try {
                    val outputStream = requireContext().contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            outputStream.close()
                        } catch (e: Exception) {
                            Toast.makeText(
                                requireContext(),
                                "saveBitmapImage: $e",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    requireContext().contentResolver.update(uri, values, null, null)
                    Toast.makeText(requireContext(), "Saved...", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Save image error: $e", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            val imageFileFolder = File(
                Environment.getExternalStorageDirectory()
                    .toString() + '/' + getString(R.string.app_name)
            )
            if (!imageFileFolder.exists()) {
                imageFileFolder.mkdirs()
            }
            val mImageName = "$timestamp.png"
            val imageFile = File(imageFileFolder, mImageName)
            try {
                val outputStream: OutputStream = FileOutputStream(imageFile)
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Save image error: $e", Toast.LENGTH_SHORT)
                        .show()
                }
                path = imageFile.absolutePath
                values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                Toast.makeText(requireContext(), "Saved...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Save image: $e", Toast.LENGTH_SHORT).show()
            }
        }
        if (uri != null) {
            shareImage(uri)
        } else {
            val image = Uri.fromFile(File(path!!))
            shareImage(image)
        }
    }
}

