package com.papb.projectpapb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.papb.projectpapb.data.model.local.TugasRepository

class MainViewModelFactory(private val tugasRepository: TugasRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(tugasRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}