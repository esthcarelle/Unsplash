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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mine.myapplication.model.PhotoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SavedPhotoDetails(photoEntity: PhotoEntity) {
    val currentSelection = remember { mutableStateOf("Original") }
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
                isBlurred.value = currentSelection.value != "Original"
            }
        )
        ImageDetail(photoEntity = photoEntity, isBlurred = isBlurred.value, imageState = photoEntity.imageState)
    }
}

@Composable
fun ImageDetail(modifier: Modifier = Modifier, isBlurred: Boolean = false, photoEntity: PhotoEntity,imageState: String = "Original") {
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val clickCount = remember { mutableStateOf(10f) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        val request = ImageRequest.Builder(LocalContext.current)
            .data(photoEntity.url)
            .build()

        if (imageState == "Original")
            AsyncImage(
                model = request,
                modifier = Modifier,
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
        else if (imageState == "Blurred") {
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
        } else if (imageState == "Zoom") {
            ZoomableComposable(url = photoEntity.url, offSetX = photoEntity.offSetX, offSetY = photoEntity.offSetY, scale = photoEntity.scale, isEditable = false)
        }
    }
}