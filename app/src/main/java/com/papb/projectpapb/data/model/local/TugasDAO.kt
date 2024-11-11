package com.papb.projectpapb.data.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TugasDAO {
    @Query("SELECT * FROM tugas")
    fun getAllTugas(): LiveData<List<Tugas>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTugas(tugas: Tugas)

    @Query("UPDATE tugas SET is_done = :is_done WHERE id = :id")
    fun updateTugas(id: Int, is_done: Boolean)

    @Query("DELETE FROM tugas WHERE id = :id")
    fun deleteTugasById(id: Int)
}

