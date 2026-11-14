package com.example.a21101373_juanalvarado_pc2.ui.main

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a21101373_juanalvarado_pc2.ui.asteroids.AsteroidsNavHost
import com.example.a21101373_juanalvarado_pc2.ui.navigation.ApodNavGraph
import com.example.a21101373_juanalvarado_pc2.ui.viewmodel.ApodViewModel

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Apod : BottomNavItem("apod_tab", "Imágenes", Icons.Default.Image)
    object Asteroids : BottomNavItem("asteroids_tab", "Asteroides", Icons.Default.Public)
}

@Composable
fun MainScreen(
    apodViewModel: ApodViewModel
) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Apod,
        BottomNavItem.Asteroids
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Apod.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Apod.route) {
                val apodNavController = rememberNavController()
                ApodNavGraph(
                    navController = apodNavController,
                    viewModel = apodViewModel
                )
            }
            composable(BottomNavItem.Asteroids.route) {
                AsteroidsNavHost()
            }
        }
    }
}
