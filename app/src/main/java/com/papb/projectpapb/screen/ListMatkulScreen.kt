package com.papb.projectpapb.screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.papb.projectpapb.R
import com.papb.projectpapb.activity.MataKuliahCard
import com.papb.projectpapb.data.model.local.MataKuliah
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mata Kuliah") },
                actions = {
                    IconButton(onClick = {
                        navController?.navigate("profile")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.github),
                            contentDescription = "GitHub Profile"
                        )
                    }
                }
            )
        },
        content = { contentPadding ->
            val schedules = remember { mutableStateListOf<MataKuliah>() }
            val db = FirebaseFirestore.getInstance()

            LaunchedEffect(Unit) {
                try {
                    val snapshot = db.collection("mata_kuliah").get().await()
                    val scheduleList = snapshot.documents.map { document ->
                        MataKuliah(
                            hari = document.getString("hari") ?: "",
                            jam = document.getString("jam") ?: "",
                            nama_matkul = document.getString("nama_matkul") ?: "",
                            ruang = document.getString("ruang") ?: "",
                            is_praktikum = document.getBoolean("is_praktikum") ?: false
                        )
                    }
                    schedules.addAll(scheduleList)
                } catch (e: Exception) {
                    Log.e("Firestore Error", "Error fetching data", e)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                items(schedules) { schedule ->
                    MataKuliahCard(schedule)
                }
            }
        }
    )
}