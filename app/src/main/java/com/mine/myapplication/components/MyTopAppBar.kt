package com.mine.myapplication

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import com.mine.myapplication.components.DropDownMenu
import com.mine.myapplication.components.DropDownMenuFrame

@Composable
fun MyTopAppBar(
    onBackClick: () -> Unit = {},
    onEditClick: (Float) -> Unit,
    onSaveClick: () -> Unit = {},
    isBlurred: Boolean = false
) {
    TopAppBar(title = { Text(text = "Details") },
        navigationIcon = {
            IconButton(onClick = { onBackClick }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        actions = {
//            IconButton(onClick = {
//                if (isBlurred)
//                    onEditClick.invoke(0.1f)
//                else onEditClick.invoke(
//                    0.1f
//                )
//            }) {
//                Icon(Icons.Default.Info, null)
//            }
            DropDownMenuFrame()

            DropDownMenu(onBlurClick = {
                if (isBlurred)
                    onEditClick.invoke(0.1f)
                else onEditClick.invoke(
                    0.1f
                )
            },
            )

            IconButton(onClick = {
                onSaveClick.invoke()
            }) {
                Icon(Icons.Default.Done, null)
            }

        })
}