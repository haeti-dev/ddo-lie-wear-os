package com.haeti.ddolie.presentation.common.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haeti.ddolie.presentation.common.manager.HealthServiceManager

class DdoLieViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DdoLieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val healthServiceManager = HealthServiceManager(context.applicationContext)
            return DdoLieViewModel(healthServiceManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}