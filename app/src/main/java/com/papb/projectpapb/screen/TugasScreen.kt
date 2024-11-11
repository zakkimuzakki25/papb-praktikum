package com.papb.projectpapb.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.papb.projectpapb.data.model.local.Tugas
import com.papb.projectpapb.viewmodel.TugasViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.shadow
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.papb.projectpapb.activity.CameraActivity

@Composable
fun TugasScreen(viewModel: TugasViewModel = viewModel()) {
    var mataKuliah by remember { mutableStateOf(TextFieldValue("")) }
    var detailTugas by remember { mutableStateOf(TextFieldValue("")) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    val tugasList by viewModel.tugasList.observeAsState(emptyList())
    val context = LocalContext.current

    // CameraX launcher
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == CameraActivity.CAMERAX_RESULT) {
            imageUri = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
        }
    }

    val isEnabled = mataKuliah.text.isNotEmpty() && detailTugas.text.isNotEmpty()

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
                .padding(bottom = 8.dp)
        )

        // Image Preview
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Preview Gambar",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 16.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { /* Launch CameraX Activity */ cameraLauncher.launch(Intent(context, CameraActivity::class.java)) }) {
                Text("Ambil Foto")
            }
            Button(
                onClick = {
                    if (isEnabled) {
                        val newTugas = Tugas(
                            matkul = mataKuliah.text,
                            detail_tugas = detailTugas.text,
                            image_uri = imageUri.toString(), // Store image URI if available
                            is_done = false
                        )
                        viewModel.insertTugas(newTugas)
                        isSubmitted = true
                        mataKuliah = TextFieldValue("")
                        detailTugas = TextFieldValue("")
                        imageUri = null
                    }
                },
                enabled = isEnabled, // Enable or disable button based on field values
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            ) {
                Text("Submit", color = if (isEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
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
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
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
    var isFullScreen by remember { mutableStateOf(false) }

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

                tugas.image_uri?.let { imageUri ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (isFullScreen) 300.dp else 150.dp)
                            .padding(vertical = 8.dp)
                            .clickable { isFullScreen = !isFullScreen }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri.toUri()),
                            contentDescription = "Preview Gambar Tugas",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (isFullScreen) 300.dp else 150.dp)
                                .padding(if (isFullScreen) 0.dp else 8.dp)
                        )
                    }
                }
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

