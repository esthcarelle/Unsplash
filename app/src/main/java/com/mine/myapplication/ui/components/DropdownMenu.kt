package com.mine.myapplication.ui.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun DropDownMenu(
    onBlurClick: () -> Unit = {},
    onZoomClick: () -> Unit = {},
    onRevert: () -> Unit = {},
    onLandScape: () -> Unit = {},
    onPortrait:() -> Unit = {}
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "More"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            content = { Text("Blurr") },
            onClick = { onBlurClick.invoke() }
        )
        DropdownMenuItem(
            content = { Text("Zoom") },
            onClick = { onZoomClick.invoke() }
        )
        DropdownMenuItem(
            content = { Text("Revert") },
            onClick = { onRevert.invoke() }
        )
        DropdownMenuItem(
            content = { Text("Landscape") },
            onClick = { onLandScape.invoke() }
        )
        DropdownMenuItem(
            content = { Text("Portrait") },
            onClick = { onPortrait.invoke() }
        )
    }
}

@Composable
fun DropDownMenuFrame(onBlack: () -> Unit = {},onGold: () -> Unit = {},onLight: () -> Unit = {},onDark: () -> Unit = {}) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "More"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            content = { Text("Black Wood") },
            onClick = { onBlack.invoke() }
        )
        DropdownMenuItem(
            content = { Text("Gold Wood") },
            onClick = { onGold.invoke() }
        )
        DropdownMenuItem(
            content = { Text("Light Wood") },
            onClick = { onLight.invoke()}
        )
        DropdownMenuItem(
            content = { Text("Dark Wood") },
            onClick = { onDark.invoke() }
        )
    }
}