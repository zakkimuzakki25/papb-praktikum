package com.papb.projectpapb.data.model.local

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TugasRepository(application: Application) {
    private val mTugasDao: TugasDAO
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = TugasDB.getDatabase(application)
        mTugasDao = db.tugasDao()
    }

    fun getAllTugas(): LiveData<List<Tugas>> = mTugasDao.getAllTugas()

    fun insert(tugas: Tugas) {
        executorService.execute { mTugasDao.insertTugas(tugas) }
    }
}
