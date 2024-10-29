package com.papb.projectpapb.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.papb.projectpapb.navigation.AppNavHost
import com.papb.projectpapb.data.model.network.MataKuliah
import com.papb.projectpapb.ui.theme.ProjectPAPBTheme

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
