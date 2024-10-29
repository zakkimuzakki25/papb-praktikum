package com.papb.projectpapb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.papb.projectpapb.data.model.local.Tugas
import com.papb.projectpapb.data.model.local.TugasRepository

class MainViewModel(private val tugasRepository: TugasRepository) : ViewModel() {
    fun getAllTugas(): LiveData<List<Tugas>> = tugasRepository.getAllTugas()

    fun insertTugas(tugas: Tugas) {
        tugasRepository.insert(tugas)
    }
}
