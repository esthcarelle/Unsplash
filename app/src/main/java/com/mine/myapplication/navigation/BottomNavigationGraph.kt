package com.mine.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mine.myapplication.components.BottomNavItem

@Composable
fun BottomNavigationGraph(navController: NavHostController) {

    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Home.screen_route) {
            PhotosNavGraph()
        }
        composable(BottomNavItem.Saved.screen_route) {
            SavedPhotoNavGraph()
        }
    }
}
