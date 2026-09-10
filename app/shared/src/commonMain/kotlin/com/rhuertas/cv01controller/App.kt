package com.rhuertas.cv01controller

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val imagePicker = rememberImagePicker()
        val projectStorage = rememberProjectStorage()
        var project by remember(projectStorage) {
            mutableStateOf(projectStorage.load()?.project ?: LaserProject())
        }

        LaunchedEffect(project) {
            projectStorage.save(PersistedProject(project))
        }

        LaserCanvas(
            project = project,
            updateProject = { project = it },
            imagePicker = imagePicker,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
