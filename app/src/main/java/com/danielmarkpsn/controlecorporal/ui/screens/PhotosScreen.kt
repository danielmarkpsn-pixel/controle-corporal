package com.danielmarkpsn.controlecorporal.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.json.JSONArray

private const val PREFS = "evolucao_fotos"
private const val KEY = "uris"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosScreen(onVoltar: () -> Unit) {
    val context = LocalContext.current
    var photos by remember { mutableStateOf(loadPhotos(context)) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        try {
            context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } catch (_: Exception) {}
        photos = (photos + uri.toString()).distinct()
        savePhotos(context, photos)
    }
    fun escolher() = picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fotos de evolução") },
                navigationIcon = { IconButton(onClick = onVoltar) { Icon(Icons.Default.ArrowBack, contentDescription = "Voltar") } },
                actions = { IconButton(onClick = ::escolher) { Icon(Icons.Default.AddAPhoto, contentDescription = "Adicionar foto") } }
            )
        }
    ) { padding ->
        if (photos.isEmpty()) {
            Column(Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.Center) {
                Text("Registre fotos para acompanhar sua evolução visual.")
                Spacer(Modifier.height(12.dp))
                Button(onClick = ::escolher) { Text("Adicionar primeira foto") }
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(photos) { value ->
                    Card(Modifier.fillMaxWidth()) {
                        Column {
                            AsyncImage(
                                model = Uri.parse(value),
                                contentDescription = "Foto de evolução",
                                modifier = Modifier.fillMaxWidth().height(280.dp),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(onClick = {
                                photos = photos.filterNot { it == value }
                                savePhotos(context, photos)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir foto")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun loadPhotos(context: Context): List<String> {
    val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "[]") ?: "[]"
    return try {
        val json = JSONArray(raw)
        List(json.length()) { json.getString(it) }
    } catch (_: Exception) { emptyList() }
}

private fun savePhotos(context: Context, photos: List<String>) {
    val json = JSONArray()
    photos.forEach(json::put)
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
        .putString(KEY, json.toString()).apply()
}
