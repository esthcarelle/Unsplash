package com.mine.myapplication.components

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mine.myapplication.R
import com.mine.myapplication.components.Constants.BLACK_FRAME
import com.mine.myapplication.components.Constants.BLURRED
import com.mine.myapplication.components.Constants.DARK_FRAME
import com.mine.myapplication.components.Constants.GOLD_FRAME
import com.mine.myapplication.components.Constants.LANDSCAPE
import com.mine.myapplication.components.Constants.LIGHT_FRAME
import com.mine.myapplication.components.Constants.ORIGINAL
import com.mine.myapplication.components.Constants.PORTRAIT
import com.mine.myapplication.components.Constants.ZOOM
import com.mine.myapplication.model.PhotoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SavedPhotoDetails(photoEntity: PhotoEntity) {
    val currentSelection = remember { mutableStateOf("Image") }
    val isBlurred = remember {
        mutableStateOf(false)
    }

    Column() {
        MultiToggleButton(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(16.dp),
            currentSelection = currentSelection.value,
            toggleStates = listOf("Image"),
            onToggleChange = {
                currentSelection.value = it
                isBlurred.value = currentSelection.value != ORIGINAL
            }
        )
        ImageDetail(photoEntity = photoEntity, isBlurred = isBlurred.value, imageState = photoEntity.imageState)
    }
}

@Composable
fun ImageDetail(modifier: Modifier = Modifier, isBlurred: Boolean = false, photoEntity: PhotoEntity,imageState: String) {
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val clickCount = remember { mutableStateOf(1f) }

    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        val request = ImageRequest.Builder(LocalContext.current)
            .data(photoEntity.url)
            .build()

        if (imageState == ORIGINAL || imageState == LANDSCAPE || imageState == PORTRAIT)
            AsyncImage(
                model = request,
                modifier = Modifier.graphicsLayer(rotationZ = photoEntity.rotation),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
        else if (imageState == BLURRED) {
            LaunchedEffect(imageState) {
                withContext(Dispatchers.IO) {
                    val drawable =
                        uriToBitmap(context, photoEntity.url)
                    bitmapState.value = drawable
                }
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                bitmapState.value?.let { it1 -> LegacyBlurImage(it1, clickCount.value) }
            } else {
                bitmapState.value?.let { it1 ->
                    BlurImage(
                        it1,
                        Modifier
                            .fillMaxSize()
                            .blur(radiusX = clickCount.value.dp, radiusY = clickCount.value.dp)
                    )
                }
            }
        } else if (imageState == ZOOM) {
            ZoomableComposable(url = photoEntity.url, offSetX = photoEntity.offSetX, offSetY = photoEntity.offSetY, scale = photoEntity.scale, isEditable = false)
        } else {
            var painter: Painter = painterResource(id = R.drawable.black_frame)
            when (imageState) {
                BLACK_FRAME -> painter = painterResource(id = R.drawable.black_frame)
                DARK_FRAME -> painter = painterResource(id = R.drawable.dark_wood_frame)
                GOLD_FRAME -> painter = painterResource(id = R.drawable.gold_frame)
                LIGHT_FRAME -> painter = painterResource(id = R.drawable.light_wood_frame)
            }
            FrameComposable(
                url = photoEntity.url,
                imagePainter = painter,
                modifier = Modifier.wrapContentSize()
            )
        }
    }
}