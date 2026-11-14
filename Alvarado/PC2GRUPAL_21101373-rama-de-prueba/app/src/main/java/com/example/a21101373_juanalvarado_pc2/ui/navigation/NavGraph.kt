package com.example.a21101373_juanalvarado_pc2.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.a21101373_juanalvarado_pc2.ui.screens.ApodDetailScreen
import com.example.a21101373_juanalvarado_pc2.ui.screens.ApodFavoritesScreen
import com.example.a21101373_juanalvarado_pc2.ui.screens.ApodListScreen
import com.example.a21101373_juanalvarado_pc2.ui.screens.AsteroidDetailScreen
import com.example.a21101373_juanalvarado_pc2.ui.screens.AsteroidFavoritesScreen
import com.example.a21101373_juanalvarado_pc2.ui.screens.AsteroidListScreen
import com.example.a21101373_juanalvarado_pc2.ui.viewmodel.ApodViewModel
import com.example.a21101373_juanalvarado_pc2.ui.viewmodel.AsteroidViewModel

object Screen {
    const val APOD_LIST = "apod_list"
    const val APOD_DETAIL = "apod_detail/{date}"
    const val APOD_FAVORITES = "apod_favorites"

    const val ASTEROID_LIST = "asteroid_list"
    const val ASTEROID_DETAIL = "asteroid_detail/{id}"
    const val ASTEROID_FAVORITES = "asteroid_favorites"

    fun apodDetail(date: String) = "apod_detail/$date"
    fun asteroidDetail(id: String) = "asteroid_detail/$id"
}

@Composable
fun MainScreen(
    apodViewModel: ApodViewModel,
    asteroidViewModel: AsteroidViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isApodSection = currentRoute == Screen.APOD_LIST ||
            currentRoute == Screen.APOD_DETAIL ||
            currentRoute == Screen.APOD_FAVORITES

    val isAsteroidSection = currentRoute == Screen.ASTEROID_LIST ||
            currentRoute == Screen.ASTEROID_DETAIL ||
            currentRoute == Screen.ASTEROID_FAVORITES

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = isApodSection,
                    onClick = {
                        if (currentRoute != Screen.APOD_LIST) {
                            navController.navigate(Screen.APOD_LIST) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.Image, contentDescription = "Imágenes") },
                    label = { Text("Imágenes") }
                )
                NavigationBarItem(
                    selected = isAsteroidSection,
                    onClick = {
                        if (currentRoute != Screen.ASTEROID_LIST) {
                            navController.navigate(Screen.ASTEROID_LIST) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.Public, contentDescription = "Asteroides") },
                    label = { Text("Asteroides") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AppNavGraph(
                navController = navController,
                apodViewModel = apodViewModel,
                asteroidViewModel = asteroidViewModel
            )
        }
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    apodViewModel: ApodViewModel,
    asteroidViewModel: AsteroidViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.APOD_LIST,
        modifier = Modifier.fillMaxSize()
    ) {
        // --- APOD Section ---
        composable(Screen.APOD_LIST) {
            ApodListScreen(
                viewModel = apodViewModel,
                onItemClick = { date ->
                    navController.navigate(Screen.apodDetail(date))
                },
                onGoToFavorites = {
                    navController.navigate(Screen.APOD_FAVORITES)
                }
            )
        }

        composable(
            route = Screen.APOD_DETAIL,
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            ApodDetailScreen(
                date = date,
                viewModel = apodViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.APOD_FAVORITES) {
            ApodFavoritesScreen(
                viewModel = apodViewModel,
                onItemClick = { date ->
                    navController.navigate(Screen.apodDetail(date))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- ASTEROIDS Section ---
        composable(Screen.ASTEROID_LIST) {
            AsteroidListScreen(
                viewModel = asteroidViewModel,
                onItemClick = { id ->
                    navController.navigate(Screen.asteroidDetail(id))
                },
                onGoToFavorites = {
                    navController.navigate(Screen.ASTEROID_FAVORITES)
                }
            )
        }

        composable(
            route = Screen.ASTEROID_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            AsteroidDetailScreen(
                id = id,
                viewModel = asteroidViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.ASTEROID_FAVORITES) {
            AsteroidFavoritesScreen(
                viewModel = asteroidViewModel,
                onItemClick = { id ->
                    navController.navigate(Screen.asteroidDetail(id))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
