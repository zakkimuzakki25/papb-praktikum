package com.papb.projectpapb

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.papb.projectpapb.ui.theme.ProjectPAPBTheme
import kotlinx.coroutines.tasks.await

class ListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectPAPBTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mata Kuliah") },
                actions = {
                    IconButton(onClick = {
                        navController?.navigate("githubProfile")
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

@Composable
fun MataKuliahCard(mataKuliah: MataKuliah) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Mata Kuliah: ${mataKuliah.nama_matkul}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Hari: ${mataKuliah.hari}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Jam: ${mataKuliah.jam}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Ruang: ${mataKuliah.ruang}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            if (mataKuliah.is_praktikum) {
                Text(
                    text = "Praktikum: Yes",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            } else {
                Text(
                    text = "Praktikum: No",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMataKuliahCard() {
    ProjectPAPBTheme {
        MataKuliahCard(
            mataKuliah = MataKuliah(
                hari = "Senin",
                jam = "08:00 - 10:00",
                nama_matkul = "Algoritma dan Struktur Data",
                ruang = "Lab 1",
                is_praktikum = true
            )
        )
    }
}
