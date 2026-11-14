package com.example.a21101373_juanalvarado_pc2.ui.asteroids

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidEntity

@Composable
fun AsteroidCard(
    asteroid: AsteroidEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hazardous = asteroid.isHazardous
    val scheme = MaterialTheme.colorScheme

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (hazardous) scheme.errorContainer else scheme.surfaceVariant
        ),
        border = if (hazardous) BorderStroke(2.dp, scheme.error) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = asteroid.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (hazardous) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = scheme.error
                        )
                        Text(
                            text = "  Potencialmente peligroso",
                            style = MaterialTheme.typography.labelLarge,
                            color = scheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text("Aproximación: ${asteroid.approachDate}")
                Text("Diámetro máx.: ${asteroid.diameterMaxM.withUnit("m")}")
                Text("Velocidad: ${asteroid.velocityKmh.withUnit("km/h")}")
                Text("Distancia: ${asteroid.missDistanceKm.withUnit("km")}")
            }
            FavoriteButton(isFavorite = asteroid.isFavorite, onClick = onToggleFavorite)
        }
    }
}

@Composable
fun FavoriteButton(isFavorite: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavorite) "Quitar de favoritos" else "Marcar como favorito",
            tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
