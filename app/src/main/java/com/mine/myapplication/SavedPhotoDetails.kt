package com.mine.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun SavedPhotoDetails(url: String){
    Column(){
        MultiToggleButton(currentSelection = "Original", toggleStates = listOf("Original","Blurred"), onToggleChange = {})
    }
}