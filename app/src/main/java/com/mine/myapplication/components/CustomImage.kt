package com.mine.myapplication.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mine.myapplication.model.PhotoEntity
import com.mine.myapplication.utils.Constants.BLACK_FRAME
import com.mine.myapplication.utils.Constants.BLURRED
import com.mine.myapplication.utils.Constants.DARK_FRAME
import com.mine.myapplication.utils.Constants.GOLD_FRAME
import com.mine.myapplication.utils.Constants.LIGHT_FRAME
import com.mine.myapplication.utils.FrameUtil

@Composable
fun CustomImage(
    modifier: Modifier,
    url: String,
    photoEntity: PhotoEntity,
    isEditable: Boolean,
    imagePainter: Painter,
    alpha: Float = 0f,
    onZoom: (Float, Float, Float) -> Unit = { _, _, _ -> }
) {
    var scale by remember { mutableStateOf(photoEntity.scale) }
    var offsetX by remember { mutableStateOf(photoEntity.offSetX) }
    var offsetY by remember { mutableStateOf(photoEntity.offSetY) }
    val context = LocalContext.current
    val request = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .build()

    var modifier: Modifier = modifier
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
    Box(
        modifier = modifier
            .height(400.dp)
            .padding(16.dp)
    ) {
        Card(
            modifier = modifier
                .padding(8.dp),
            shape = RoundedCornerShape(6.dp)
        ) {
            if (photoEntity.imageState == BLURRED || photoEntity.imageState == BLACK_FRAME || photoEntity.imageState == GOLD_FRAME || photoEntity.imageState == DARK_FRAME || photoEntity.imageState == LIGHT_FRAME) {

                var frameBitmap : Bitmap = BitmapFactory.decodeResource(
                        context.resources,
                        FrameUtil.painterToFrame(photoEntity.imageState)
                    )

                var urlToBitmap =
                frameBitmap?.asImageBitmap()?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                    )
                }
            } else
                AsyncImage(
                    model = request,
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
}