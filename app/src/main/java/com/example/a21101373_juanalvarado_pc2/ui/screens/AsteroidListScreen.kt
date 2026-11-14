package com.example.a21101373_juanalvarado_pc2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidEntity
import com.example.a21101373_juanalvarado_pc2.ui.viewmodel.AsteroidViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidListScreen(
    viewModel: AsteroidViewModel,
    onItemClick: (String) -> Unit,
    onGoToFavorites: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val filteredList = if (uiState.filterOnlyHazardous) {
        uiState.asteroids.filter { it.isPotentiallyHazardous }
    } else {
        uiState.asteroids
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Asteroides (NeoWs)") },
                actions = {
                    IconButton(onClick = onGoToFavorites) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Mis Asteroides",
                            tint = Color.Red
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter Switch Header
            Surface(
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Solo potencialmente peligrosos",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = uiState.filterOnlyHazardous,
                        onCheckedChange = { viewModel.setFilterOnlyHazardous(it) }
                    )
                }
            }

            uiState.errorMessage?.let { error ->
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading && uiState.asteroids.isEmpty() -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    filteredList.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (uiState.filterOnlyHazardous) {
                                    "No hay asteroides potencialmente peligrosos en los últimos 7 días."
                                } else {
                                    "No se encontraron asteroides."
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.refreshAsteroids() }) {
                                Text("Reintentar")
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredList, key = { it.id }) { asteroid ->
                                AsteroidItemCard(
                                    asteroid = asteroid,
                                    onItemClick = { onItemClick(asteroid.id) },
                                    onToggleFavorite = { viewModel.toggleFavorite(asteroid.id) }
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
fun AsteroidItemCard(
    asteroid: AsteroidEntity,
    onItemClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val cardColors = if (asteroid.isPotentiallyHazardous) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
        )
    } else {
        CardDefaults.cardColors()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = cardColors
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = asteroid.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (asteroid.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (asteroid.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (asteroid.isPotentiallyHazardous) {
                AssistChip(
                    onClick = { },
                    label = { Text("¡Potencialmente Peligroso!", fontWeight = FontWeight.Bold) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Peligro",
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        labelColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Text(
                text = "Aproximación: ${asteroid.closeApproachDate}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Diámetro máx: ${String.format(Locale.US, "%.2f m", asteroid.estimatedDiameterMaxMeters)}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Velocidad: ${String.format(Locale.US, "%.2f km/h", asteroid.relativeVelocityKmH)}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Distancia: ${String.format(Locale.US, "%.2f km", asteroid.missDistanceKm)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
