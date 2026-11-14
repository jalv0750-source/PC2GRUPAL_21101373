package com.example.a21101373_juanalvarado_pc2.ui.asteroids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteAsteroidsScreen(
    favorites: List<AsteroidEntity>,
    onBack: () -> Unit,
    onAsteroidClick: (AsteroidEntity) -> Unit,
    onToggleFavorite: (AsteroidEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis asteroides") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (favorites.isEmpty()) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                EmptyMessage("Aún no tienes asteroides favoritos.\nMárcalos con el corazón desde la lista.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favorites, key = { it.id }) { asteroid ->
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
