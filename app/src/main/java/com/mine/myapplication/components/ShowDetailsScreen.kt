package com.mine.myapplication.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.mine.myapplication.model.PhotoEntity
import com.mine.myapplication.viewModel.SavedPhotoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ShowImageDetails(url: String, onBackClick: () -> Unit = {}, viewModel: SavedPhotoViewModel) {
    val clickCount = remember { mutableStateOf(0.1f) }
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val imageState = remember { mutableStateOf("Original") }

    Scaffold(topBar = {
        MyTopAppBar(onBackClick = onBackClick, onEditClick = {
            clickCount.value = it
            imageState.value = imageState.value
        }, onBlurClick = { imageState.value = "Blurred"
            clickCount.value = it
                         },
            onZoomClick = { imageState.value = "Zoom" },
            onRevertClick = {imageState.value = "Original"},
            imageState = imageState.value,
            onSaveClick = {
                val photoEntity = PhotoEntity(url = url)
                viewModel.saveImage(photoEntity)
                Toast.makeText(context, "Successfully Saved", Toast.LENGTH_LONG).show()
            }
        )
    }, content = {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            val request = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .build()

            if (imageState.value == "Original")
                AsyncImage(
                    model = request,
                    modifier = Modifier,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            else if (imageState.value == "Blurred") {
                LaunchedEffect(imageState.value) {
                    withContext(Dispatchers.IO) {
                        val drawable =
                            uriToBitmap(context, url)
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
            } else if (imageState.value == "Zoom") {
                ZoomableComposable(url = url, offSetX = 0f, offSetY = 0f, scale = 1f)
            }
        }

    })
}

suspend fun uriToBitmap(context: Context, uri: String?): Bitmap {

    val loader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(uri)
        .allowHardware(false) // Disable hardware bitmaps.
        .build()

    val result = (loader.execute(request) as SuccessResult).drawable
    val bitmap = (result as BitmapDrawable).bitmap

    return Bitmap.createScaledBitmap(
        bitmap, 100, 100, true
    )
}

@Composable
fun LegacyBlurImage(
    bitmap: Bitmap,
    blurRadio: Float = 25f,
    modifier: Modifier = Modifier.fillMaxSize()
) {

    val renderScript = RenderScript.create(LocalContext.current)
    val bitmapAlloc =
        Allocation.createFromBitmap(renderScript, bitmap)
    ScriptIntrinsicBlur.create(renderScript, bitmapAlloc.element).apply {
        setRadius(blurRadio)
        setInput(bitmapAlloc)
        forEach(bitmapAlloc)
    }
    bitmapAlloc.copyTo(bitmap)
    renderScript.destroy()

    BlurImage(bitmap, modifier)
}

@Composable
fun BlurImage(
    bitmap: Bitmap,
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}
