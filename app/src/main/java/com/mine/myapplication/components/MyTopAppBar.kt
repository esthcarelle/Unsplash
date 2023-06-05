package com.mine.myapplication.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable

@Composable
fun MyTopAppBar(
    onBackClick: () -> Unit = {},
    onEditClick: (Float) -> Unit,
    onSaveClick: () -> Unit = {},
    onBlurClick:(Float) -> Unit = {},
    onZoomClick: () -> Unit = {},
    onRevertClick:() -> Unit ={},
    imageState: String = "Original"
) {
    TopAppBar(title = { Text(text = "Details") },
        navigationIcon = {
            IconButton(onClick = { onBackClick }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        actions = {
            DropDownMenuFrame()

            DropDownMenu(onBlurClick = {
                    onBlurClick.invoke(10f)
            },
                onRevert = { onRevertClick.invoke() },
                onZoomClick = { onZoomClick.invoke() }
            )

            IconButton(onClick = {
                onSaveClick.invoke()
            }) {
                Icon(Icons.Default.Done, null)
            }

        })
}