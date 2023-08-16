package com.example.androidcrown.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoadingViewModel: ViewModel() {
    private val _loadingState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean>
        get() = _loadingState

    fun loaded() {
        _loadingState.value = true
    }
}