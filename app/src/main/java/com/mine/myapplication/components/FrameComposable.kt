package com.mine.myapplication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun FrameComposable(modifier: Modifier,url: String, imagePainter: Painter) {
    Box (modifier = modifier.height(400.dp)){
        Image(painter = imagePainter, contentScale = ContentScale.FillBounds, contentDescription = "")
        Card(
            modifier = Modifier
                .height(400.dp)
                .padding(8.dp),
            shape = RoundedCornerShape(4.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .build(),
                modifier = Modifier,
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
        }
    }
}