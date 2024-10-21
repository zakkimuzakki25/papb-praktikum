package com.papb.projectpapb.data.model.local

data class MataKuliah(
    val hari: String,
    val jam: String,
    var nama_matkul: String,
    val ruang: String,
    var is_praktikum: Boolean,
)