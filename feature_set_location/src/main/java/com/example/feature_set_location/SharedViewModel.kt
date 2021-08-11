package com.example.feature_set_location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<String>()

    fun select(country: String) {
        selected.value = country
    }

    fun getSelected(): LiveData<String>? {
        return selected
    }
}