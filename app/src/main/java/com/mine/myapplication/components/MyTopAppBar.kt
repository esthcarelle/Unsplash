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
    onSaveClick: () -> Unit = {},
    onBlurClick: (Float) -> Unit = {},
    onZoomClick: () -> Unit = {},
    onRevertClick: () -> Unit = {},
    onBlack: () -> Unit = {},
    onGold: () -> Unit = {},
    onLight: () -> Unit = {},
    onDark: () -> Unit = {},
    onLandScape: () -> Unit = {},
    onPortrait:() -> Unit = {}
) {
    TopAppBar(title = { Text(text = "Details") },
        navigationIcon = {
            IconButton(onClick = { onBackClick }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        actions = {
            DropDownMenuFrame(
                onDark = { onDark.invoke() },
                onGold = { onGold.invoke() },
                onBlack = { onBlack.invoke() },
                onLight = { onLight.invoke() })

            DropDownMenu(onBlurClick = {
                onBlurClick.invoke(1f)
            },
                onRevert = { onRevertClick.invoke() },
                onZoomClick = { onZoomClick.invoke() },
                onLandScape = { onLandScape.invoke() },
                onPortrait = { onPortrait.invoke() }
            )
            IconButton(onClick = {
                onSaveClick.invoke()
            }) {
                Icon(Icons.Default.Done, null)
            }
        })
}