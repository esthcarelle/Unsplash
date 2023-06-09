package com.mine.myapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title:String, var icon:ImageVector, var screen_route:String){
    object Home : BottomNavItem("Home", Icons.Default.Home,"Home")
    object Saved: BottomNavItem("Saved",Icons.Default.Favorite,"Saved")
}