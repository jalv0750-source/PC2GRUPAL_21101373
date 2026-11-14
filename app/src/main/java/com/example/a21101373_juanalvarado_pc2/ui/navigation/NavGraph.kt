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
import com.example.a21101373_juanalvarado_pc2.ui.screens.AsteroidsScreen
import com.example.a21101373_juanalvarado_pc2.ui.viewmodel.ApodViewModel

object Screen {
    const val LIST = "apod_list"
    const val ASTEROIDS = "asteroids"
    const val DETAIL = "apod_detail/{date}"
    const val FAVORITES = "apod_favorites"

    fun detail(date: String) = "apod_detail/$date"
}

@Composable
fun MainScreen(viewModel: ApodViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Screen.LIST || currentRoute == Screen.DETAIL || currentRoute == Screen.FAVORITES,
                    onClick = {
                        if (currentRoute != Screen.LIST) {
                            navController.navigate(Screen.LIST) {
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
                    selected = currentRoute == Screen.ASTEROIDS,
                    onClick = {
                        if (currentRoute != Screen.ASTEROIDS) {
                            navController.navigate(Screen.ASTEROIDS) {
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
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: ApodViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LIST,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.LIST) {
            ApodListScreen(
                viewModel = viewModel,
                onItemClick = { date ->
                    navController.navigate(Screen.detail(date))
                },
                onGoToFavorites = {
                    navController.navigate(Screen.FAVORITES)
                }
            )
        }

        composable(Screen.ASTEROIDS) {
            AsteroidsScreen()
        }

        composable(
            route = Screen.DETAIL,
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            ApodDetailScreen(
                date = date,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.FAVORITES) {
            ApodFavoritesScreen(
                viewModel = viewModel,
                onItemClick = { date ->
                    navController.navigate(Screen.detail(date))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
