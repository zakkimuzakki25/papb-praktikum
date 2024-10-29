package com.papb.projectpapb.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.papb.projectpapb.data.model.local.TugasDB
import com.papb.projectpapb.data.model.local.Tugas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TugasViewModel(application: Application) : AndroidViewModel(application) {
    private val tugasDao = TugasDB.getDatabase(application).tugasDao()
    val tugasList: LiveData<List<Tugas>> = tugasDao.getAllTugas()

    fun insertTugas(tugas: Tugas) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                tugasDao.insertTugas(tugas)
                Log.d("TugasViewModel", "Tugas berhasil disimpan: $tugas")
            } catch (e: Exception) {
                Log.e("TugasViewModel", "Error saat menyimpan tugas: ${e.message}")
            }
        }
    }

    fun updateTugasStatus(id: Int, isDone: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                tugasDao.updateTugas(id, isDone)
                Log.d("TugasViewModel", "Status tugas dengan id $id diubah menjadi $isDone")
            } catch (e: Exception) {
                Log.e("TugasViewModel", "Error saat mengupdate status tugas: ${e.message}")
            }
        }
    }

    fun deleteTugas(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                tugasDao.deleteTugasById(id)
                Log.d("TugasViewModel", "Tugas dengan id $id berhasil dihapus")
            } catch (e: Exception) {
                Log.e("TugasViewModel", "Error saat menghapus tugas: ${e.message}")
            }
        }
    }
}
