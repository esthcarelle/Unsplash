package com.mine.myapplication.ui.components

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ZoomableComposable(
    isEditable: Boolean = true,
    url: String,
    scale: Float,
    offSetX: Float,
    offSetY: Float,
    onZoom: (Float, Float, Float) -> Unit = { _, _, _ -> }
) {
    var scale by remember { mutableStateOf(scale) }
    var offsetX by remember { mutableStateOf(offSetX) }
    var offsetY by remember { mutableStateOf(offSetY) }

    // In the example below, we make the Column composable zoomable
    // by leveraging the Modifier.pointerInput modifier
    var modifier: Modifier = Modifier
    if (isEditable)
        modifier = Modifier
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        awaitFirstDown()
                        do {
                            val event = awaitPointerEvent()
                            scale *= event.calculateZoom()
                            val offset = event.calculatePan()
                            offsetX += offset.x
                            offsetY += offset.y
                            onZoom.invoke(offsetX, offsetY, scale)
                        } while (event.changes.any { it.pressed })
                    }
                }
            }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .build(),
            modifier = Modifier.graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            ),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
    }
}