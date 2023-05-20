package com.bejussi.dailyadvice.presentation.advice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.core.util.makeToast
import com.bejussi.dailyadvice.databinding.FragmentAdviceBinding
import com.bejussi.dailyadvice.presentation.core.UIEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdviceFragment : Fragment() {

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
                when(event) {
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
    }
}