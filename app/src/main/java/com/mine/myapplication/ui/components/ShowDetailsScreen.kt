package com.mine.myapplication.ui.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.mine.myapplication.model.PhotoEntity
import com.mine.myapplication.viewModel.SavedPhotoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.mine.myapplication.R
import com.mine.myapplication.utils.Constants.BLACK_FRAME
import com.mine.myapplication.utils.Constants.BLURRED
import com.mine.myapplication.utils.Constants.DARK_FRAME
import com.mine.myapplication.utils.Constants.GOLD_FRAME
import com.mine.myapplication.utils.Constants.LANDSCAPE
import com.mine.myapplication.utils.Constants.LIGHT_FRAME
import com.mine.myapplication.utils.Constants.ORIGINAL
import com.mine.myapplication.utils.Constants.PORTRAIT
import com.mine.myapplication.utils.Constants.ZOOM
import com.mine.myapplication.utils.FrameUtil

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ShowImageDetails(
    onSaveClick: () -> Unit = {},
    url: String,
    onBackClick: () -> Unit = {},
    viewModel: SavedPhotoViewModel
) {
    val clickCount = remember { mutableStateOf(0.1f) }
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val imageState = remember { mutableStateOf(ORIGINAL) }
    val offSetY = remember {
        mutableStateOf(0f)
    }
    val offSetX = remember {
        mutableStateOf(0f)
    }
    val scale = remember { mutableStateOf(1f) }
    val rotationZ = remember { mutableStateOf(0f) }

    Scaffold(topBar = {
        MyTopAppBar(onBackClick = onBackClick, onBlurClick = {
            imageState.value = BLURRED
            clickCount.value = it
        },
            onZoomClick = { imageState.value = ZOOM },
            onRevertClick = { imageState.value = ORIGINAL },
            onDark = { imageState.value = DARK_FRAME },
            onLight = { imageState.value = LIGHT_FRAME },
            onGold = { imageState.value = GOLD_FRAME },
            onBlack = { imageState.value = BLACK_FRAME },
            onLandScape = {
                imageState.value = LANDSCAPE
                rotationZ.value = 90f
            },
            onPortrait = {
                imageState.value = PORTRAIT
                rotationZ.value = 0f
            },
            onSaveClick = {

                val photoEntity = PhotoEntity(url = url)

                photoEntity.imageState = imageState.value
                photoEntity.offSetX = offSetX.value
                photoEntity.offSetY = offSetY.value
                photoEntity.scale = scale.value
                photoEntity.rotation = rotationZ.value

                viewModel.saveImage(photoEntity)

                Toast.makeText(context, "Successfully Saved", Toast.LENGTH_LONG).show()
                onSaveClick.invoke()
            }
        )
    }, content = {

        val request = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .build()

        when (imageState.value) {
            ORIGINAL, LANDSCAPE, PORTRAIT -> {
                var modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                CustomImage(
                    modifier = modifier,
                    url = url,
                    photoEntity = PhotoEntity(),
                    isEditable = true,
                    imagePainter = painterResource(id = R.drawable.ic_launcher_background),
                    onZoom = { zoomOffSetX, zoomOffSetY, zoomScale ->
                        offSetX.value = zoomOffSetX
                        offSetY.value = zoomOffSetY
                        scale.value = zoomScale
                    })
            }

            BLURRED -> {
                var modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                Card(
                    modifier = modifier,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    LaunchedEffect(imageState.value) {
                        withContext(Dispatchers.IO) {
                            val drawable =
                                viewModel.uriToBitmap(context, url)
                            bitmapState.value = drawable
                        }
                    }
                    CustomImage(
                        modifier = modifier,
                        url = url,
                        photoEntity = PhotoEntity(),
                        isEditable = true,
                        blurRatio = clickCount.value,
                        imagePainter = painterResource(id = R.drawable.ic_launcher_background),
                        onZoom = { zoomOffSetX, zoomOffSetY, zoomScale ->
                            offSetX.value = zoomOffSetX
                            offSetY.value = zoomOffSetY
                            scale.value = zoomScale
                        })
                }

            }

            ZOOM -> {
                CustomImage(
                    modifier = Modifier,
                    url = url,
                    photoEntity = PhotoEntity(),
                    isEditable = true,
                    imagePainter = painterResource(id = R.drawable.ic_launcher_background),
                    onZoom = { zoomOffSetX, zoomOffSetY, zoomScale ->
                        offSetX.value = zoomOffSetX
                        offSetY.value = zoomOffSetY
                        scale.value = zoomScale
                    })
            }

            else -> {
                CustomImage(
                    modifier = Modifier,
                    url = url,
                    photoEntity = PhotoEntity(),
                    isEditable = true,
                    alpha = 1f,
                    imagePainter = painterResource(id = FrameUtil.painterToFrame(imageState.value)),
                    onZoom = { zoomOffSetX, zoomOffSetY, zoomScale ->
                        offSetX.value = zoomOffSetX
                        offSetY.value = zoomOffSetY
                        scale.value = zoomScale
                    })
            }
        }
    }
    )
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
