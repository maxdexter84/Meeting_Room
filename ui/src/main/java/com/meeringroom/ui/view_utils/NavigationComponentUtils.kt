package com.meeringroom.ui.view_utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.getNavigationResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

fun Fragment.setNavigationResult(key: String, result: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}