package com.bejussi.dailyadvice.presentation.advice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.core.util.Resource
import com.bejussi.dailyadvice.core.util.StringResourcesProvider
import com.bejussi.dailyadvice.domain.AdviceRepository
import com.bejussi.dailyadvice.presentation.advice.model.AdviceState
import com.bejussi.dailyadvice.presentation.core.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdviceViewModel @Inject constructor(
    private val adviceRepository: AdviceRepository,
    private val stringResourcesProvider: StringResourcesProvider
): ViewModel() {

    private val _state = MutableStateFlow(AdviceState())
    val state = _state.asStateFlow()

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        getRandomAdvice()
    }

    fun getRandomAdvice() {
        viewModelScope.launch {
            adviceRepository.getRandomAdvice().collect { result ->
                when(result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            advice = result.data,
                            isLoading = false,
                            isError = true
                        )
                        _eventFlow.send(
                            UIEvent.ShowToast(
                                result.message ?: stringResourcesProvider.getString(R.string.unknownError)
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            advice = result.data,
                            isLoading = true,
                            isError = false
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            advice = result.data,
                            isLoading = false,
                            isError = false
                        )
                    }
                }
            }
        }
    }
}