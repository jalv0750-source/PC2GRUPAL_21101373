package com.example.a21101373_juanalvarado_pc2.ui.asteroids

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

private object AsteroidRoutes {
    const val LIST = "asteroids/list"
    const val FAVORITES = "asteroids/favorites"
    const val DETAIL = "asteroids/detail/{id}"
    fun detail(id: String) = "asteroids/detail/$id"
}

@Composable
fun AsteroidsNavHost() {
    val context = LocalContext.current.applicationContext
    val viewModel: AsteroidViewModel = viewModel(factory = AsteroidViewModelFactory(context))
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AsteroidRoutes.LIST) {

        composable(AsteroidRoutes.LIST) {
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            AsteroidListScreen(
                state = state,
                onOnlyHazardousChange = viewModel::setOnlyHazardous,
                onRefresh = viewModel::refresh,
                onAsteroidClick = { navController.navigate(AsteroidRoutes.detail(it.id)) },
                onToggleFavorite = viewModel::toggleFavorite,
                onOpenFavorites = { navController.navigate(AsteroidRoutes.FAVORITES) }
            )
        }

        composable(
            route = AsteroidRoutes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            val asteroid by remember(id) { viewModel.observeAsteroid(id) }
                .collectAsStateWithLifecycle(initialValue = null)
            AsteroidDetailScreen(
                asteroid = asteroid,
                onBack = { navController.popBackStack() },
                onToggleFavorite = viewModel::toggleFavorite
            )
        }

        composable(AsteroidRoutes.FAVORITES) {
            val favorites by viewModel.favorites.collectAsStateWithLifecycle()
            FavoriteAsteroidsScreen(
                favorites = favorites,
                onBack = { navController.popBackStack() },
                onAsteroidClick = { navController.navigate(AsteroidRoutes.detail(it.id)) },
                onToggleFavorite = viewModel::toggleFavorite
            )
        }
    }
}
