package com.mine.myapplication.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mine.myapplication.components.MultiToggleButton

@Composable
fun SavedPhotoDetails(url: String){
    val currentSelection = remember { mutableStateOf("Original") }
    Column(){
        MultiToggleButton(currentSelection = currentSelection.value, toggleStates = listOf("Original","Blurred"), onToggleChange = {currentSelection.value = it})
    }
}