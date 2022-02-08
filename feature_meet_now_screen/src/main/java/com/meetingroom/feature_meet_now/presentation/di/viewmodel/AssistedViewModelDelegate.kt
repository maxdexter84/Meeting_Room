package com.meetingroom.feature_meet_now.presentation.di.viewmodel

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@MainThread
inline fun <reified VM : ViewModel> Fragment.assistedViewModels(
    crossinline viewModelProducer: () -> VM
): Lazy<VM> {
    return lazy(LazyThreadSafetyMode.NONE) { createViewModel(viewModelProducer) }
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.createViewModel(
    crossinline viewModelProducer: () -> VM
): VM {
    val factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>) = viewModelProducer() as VM
    }
    return ViewModelProvider(this, factory).get(VM::class.java)
}