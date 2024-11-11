package com.papb.projectpapb.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
class Tugas (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "matkul")
    var matkul: String,
    @ColumnInfo(name = "detail_tugas")
    var detail_tugas: String,
    @ColumnInfo(name = "is_done")
    var is_done: Boolean,
    @ColumnInfo(name = "image_uri")
    val image_uri: String? = null
) : Parcelable