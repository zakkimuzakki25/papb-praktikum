package com.papb.projectpapb.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.papb.projectpapb.data.model.local.Tugas
import com.papb.projectpapb.viewmodel.TugasViewModel

@Composable
fun TugasScreen(viewModel: TugasViewModel = viewModel()) {
    var mataKuliah by remember { mutableStateOf(TextFieldValue("")) }
    var detailTugas by remember { mutableStateOf(TextFieldValue("")) }
    var isSubmitted by remember { mutableStateOf(false) }

    val tugasList by viewModel.tugasList.observeAsState(emptyList())

    LaunchedEffect(isSubmitted) {
        if (isSubmitted) {
            isSubmitted = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Input Tugas Kuliah",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = mataKuliah,
            onValueChange = { mataKuliah = it },
            label = { Text("Nama Mata Kuliah") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = detailTugas,
            onValueChange = { detailTugas = it },
            label = { Text("Detail Tugas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (mataKuliah.text.isNotEmpty() && detailTugas.text.isNotEmpty()) {
                    val newTugas = Tugas(
                        matkul = mataKuliah.text,
                        detail_tugas = detailTugas.text,
                        is_done = false
                    )
                    viewModel.insertTugas(newTugas)
                    isSubmitted = true
                    mataKuliah = TextFieldValue("")
                    detailTugas = TextFieldValue("")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Submit", color = MaterialTheme.colorScheme.onPrimary)
        }

        if (isSubmitted) {
            Snackbar(
                modifier = Modifier.padding(top = 8.dp),
                action = {
                    TextButton(onClick = { isSubmitted = false }) {
                        Text("Tutup", color = MaterialTheme.colorScheme.primary)
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Text("Tugas berhasil disimpan!", color = MaterialTheme.colorScheme.onSurface)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (tugasList.isNotEmpty()) {
            LazyColumn {
                items(tugasList) { tugas ->
                    TugasKuliahCard(
                        tugas = tugas,
                        onStatusChange = { viewModel.updateTugasStatus(tugas.id, true) },
                        onDelete = { viewModel.deleteTugas(tugas.id) }
                    )
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                }
            }
        } else {
            Text(
                text = "Tidak ada tugas untuk ditampilkan",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun TugasKuliahCard(tugas: Tugas, onStatusChange: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Mata Kuliah: ${tugas.matkul}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Detail Tugas: ${tugas.detail_tugas}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (tugas.is_done) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Done",
                    tint = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Hapus",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                Button(
                    onClick = onStatusChange,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text("Selesai", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
    }
}
