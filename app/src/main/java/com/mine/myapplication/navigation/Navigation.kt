package com.mine.myapplication

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.mine.myapplication.components.SavedPhotoDetails
import com.mine.myapplication.components.SavedPhotosScreen
import com.mine.myapplication.components.ShowImageDetails
import com.mine.myapplication.components.SliderView
import com.mine.myapplication.viewModel.SavedPhotoViewModel

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
            GetImagesScreen(onNavigateToDetailsScreen = { url ->
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
                ?.let { url ->
                    ShowImageDetails(
                        url = url, viewModel = (
                                SavedPhotoViewModel(
                                    LocalContext.current.applicationContext
                                            as Application
                                ))
                    )
                }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SavedPhotoNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "saved"
    ) {
        /* creating route "home" */
        composable(route = "saved") {
            /* Using composable function */
            SavedPhotosScreen(viewModel = (
                    SavedPhotoViewModel(
                        LocalContext.current.applicationContext
                                as Application
                    )
                    ), onNavigateToDetailsScreen = { url ->
                navController.currentBackStackEntry?.arguments?.putString("url", url)
                navController.navigate(
                    "details?url=${
                        Uri.encode(url)
                    }"
                )
            }
            )
        }
        composable(
            route = "details?url={url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) {
            /* Using composable function */
            val pageState = rememberPagerState()
            it.arguments?.getString("url")
                ?.let { url -> SliderView(state = pageState, url = url, viewModel = SavedPhotoViewModel(
                    LocalContext.current.applicationContext
                            as Application
                )
                ) }
        }
    }
}
