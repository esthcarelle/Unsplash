package com.mine.myapplication.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.mine.myapplication.components.MultiToggleButton

@Composable
fun SavedPhotoDetails(url: String){
    Column(){
        MultiToggleButton(currentSelection = "Original", toggleStates = listOf("Original","Blurred"), onToggleChange = {})
    }
}