package com.example.a21101373_juanalvarado_pc2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.a21101373_juanalvarado_pc2.ui.screens.ApodDetailScreen
import com.example.a21101373_juanalvarado_pc2.ui.screens.ApodFavoritesScreen
import com.example.a21101373_juanalvarado_pc2.ui.screens.ApodListScreen
import com.example.a21101373_juanalvarado_pc2.ui.viewmodel.ApodViewModel

object Screen {
    const val LIST = "apod_list"
    const val DETAIL = "apod_detail/{date}"
    const val FAVORITES = "apod_favorites"

    fun detail(date: String) = "apod_detail/$date"
}

@Composable
fun ApodNavGraph(
    navController: NavHostController,
    viewModel: ApodViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LIST
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
