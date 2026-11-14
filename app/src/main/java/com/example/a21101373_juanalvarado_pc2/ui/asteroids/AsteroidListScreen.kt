package com.example.a21101373_juanalvarado_pc2.ui.asteroids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidListScreen(
    state: AsteroidListUiState,
    onOnlyHazardousChange: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    onAsteroidClick: (AsteroidEntity) -> Unit,
    onToggleFavorite: (AsteroidEntity) -> Unit,
    onOpenFavorites: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asteroides (NeoWs)") },
                actions = {
                    IconButton(onClick = onOpenFavorites) {
                        Icon(Icons.Default.Favorite, contentDescription = "Mis asteroides")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Solo potencialmente peligrosos", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = state.onlyHazardous, onCheckedChange = onOnlyHazardousChange)
            }

            if (state.isLoading && state.asteroids.isNotEmpty()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when {
                    state.isLoading && state.asteroids.isEmpty() -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    state.asteroids.isEmpty() -> {
                        EmptyMessage(
                            text = state.errorMessage
                                ?: if (state.onlyHazardous) "No hay asteroides potencialmente peligrosos en los últimos 7 días."
                                else "No hay asteroides para mostrar.",
                            showRetry = true,
                            onRetry = onRefresh,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.asteroids, key = { it.id }) { asteroid ->
                                AsteroidCard(
                                    asteroid = asteroid,
                                    onClick = { onAsteroidClick(asteroid) },
                                    onToggleFavorite = { onToggleFavorite(asteroid) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyMessage(
    text: String,
    modifier: Modifier = Modifier,
    showRetry: Boolean = false,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = text, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        if (showRetry) {
            Button(onClick = onRetry) { Text("Reintentar") }
        }
    }
}
