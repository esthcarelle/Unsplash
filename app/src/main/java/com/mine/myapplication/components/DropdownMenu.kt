package com.mine.myapplication.components

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
    onRevert: () -> Unit = {}
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
    }
}

@Composable
fun DropDownMenuFrame() {
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
            onClick = { }
        )
        DropdownMenuItem(
            content = { Text("Gold") },
            onClick = { }
        )
        DropdownMenuItem(
            content = { Text("Light Wood") },
            onClick = { }
        )
        DropdownMenuItem(
            content = { Text("Black") },
            onClick = { }
        )
    }
}