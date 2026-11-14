package com.example.a21101373_juanalvarado_pc2.ui.asteroids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidDetailScreen(
    asteroid: AsteroidEntity?,
    onBack: () -> Unit,
    onToggleFavorite: (AsteroidEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del asteroide") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (asteroid != null) {
                        FavoriteButton(
                            isFavorite = asteroid.isFavorite,
                            onClick = { onToggleFavorite(asteroid) }
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (asteroid == null) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                EmptyMessage("No se encontró el asteroide.")
            }
            return@Scaffold
        }

        val scheme = MaterialTheme.colorScheme
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(asteroid.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (asteroid.isHazardous) scheme.errorContainer else scheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (asteroid.isHazardous) "⚠ Potencialmente peligroso" else "No es potencialmente peligroso",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (asteroid.isHazardous) scheme.error else scheme.onSecondaryContainer
                )
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DetailRow("Nombre", asteroid.name)
                    HorizontalDivider()
                    DetailRow("ID", asteroid.id)
                    HorizontalDivider()
                    DetailRow("Fecha de aproximación", asteroid.approachDate)
                    HorizontalDivider()
                    DetailRow("Diámetro mínimo estimado", asteroid.diameterMinM.withUnit("m"))
                    HorizontalDivider()
                    DetailRow("Diámetro máximo estimado", asteroid.diameterMaxM.withUnit("m"))
                    HorizontalDivider()
                    DetailRow("Velocidad relativa", asteroid.velocityKmh.withUnit("km/h"))
                    HorizontalDivider()
                    DetailRow("Distancia de paso", asteroid.missDistanceKm.withUnit("km"))
                    HorizontalDivider()
                    DetailRow("Potencialmente peligroso", if (asteroid.isHazardous) "Sí" else "No")
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}
