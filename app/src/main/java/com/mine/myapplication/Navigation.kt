package com.mine.myapplication

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController

@Composable
fun PhotosNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "random"
    ) {
        /* creating route "home" */
        composable(route = "random") {
            /* Using composable function */
            GetImagesScreen(PhotoViewModel(), onNavigateToDetailsScreen = { url ->
                navController.currentBackStackEntry?.arguments?.putString("url", url)
                navController.navigate(
                    "details?url=${
                        Uri.encode(url)
                    }"
                )
            })
        }
        composable(
            route = "details?url={url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) {
            /* Using composable function */
            it.arguments?.getString("url")
                ?.let { url -> ShowImageDetails(url) }
        }
    }
}
