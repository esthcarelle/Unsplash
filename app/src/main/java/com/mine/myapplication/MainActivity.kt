package com.mine.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.*
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.ar.core.Config
import com.mine.myapplication.ui.theme.UnsplashTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnsplashTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    NavGraph()
                }
            }
        }
    }
    @Composable
    fun HomeScreen(){
        Scaffold(bottomBar = ) {

        }
    }
    @
    @Composable
    fun NavGraph() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "random"
        ) {
            /* creating route "home" */
            composable(route = "random") {
                /* Using composable function */
                GetImagesScreen(PhotoViewModel(), onNavigateToDetailsScreen = { url ->
                    navController.currentBackStackEntry?.arguments?.putString("url", url)
                    navController.navigate(
                        "details?url=${
                            Uri.encode(url)
                        }"
                    )
                })
            }
            composable(
                route = "details?url={url}",
                arguments = listOf(navArgument("url") { type = NavType.StringType })
            ) {
                /* Using composable function */
                it.arguments?.getString("url")
                    ?.let { url -> ShowImageDetails(url) }
            }
        }
    }

    @Composable
    fun GetImagesScreen(vm: PhotoViewModel, onNavigateToDetailsScreen: (String) -> Unit) {
        LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2)) {
            items(vm.imagesList) { photo ->
                Card(
                    modifier = Modifier
                        .height(170.dp)
                        .clickable {
                            photo.urls?.regular?.let { onNavigateToDetailsScreen(it) }
                        }
                        .padding(8.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(photo.urls?.regular)
                            .build(),
                        modifier = Modifier,
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun ShowImageDetails(url: String, onBackClick: () -> Unit = {}) {
        val clickCount = remember { mutableStateOf(0.1f) }
        val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
        val context = LocalContext.current
        val isBlurred = remember { mutableStateOf(false) }

        Scaffold(topBar = {
            MyTopAppBar(onBackClick = onBackClick, onEditClick = {
                clickCount.value = it
                isBlurred.value = !isBlurred.value
            }, isBlurred.value)
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

                if(!isBlurred.value)
                AsyncImage(
                    model = request,
                    modifier = Modifier.blur(radius = 50.dp),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )

                LaunchedEffect(isBlurred.value) {
                    withContext(Dispatchers.IO) {
                        val drawable =
                            uriToBitmap(context, url)
                        bitmapState.value = drawable
                    }
                }

                if(isBlurred.value)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    Log.e(TAG, "ShowImageDetails: " + clickCount.value)
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
            }


        })
    }

    private suspend fun uriToBitmap(context: Context, uri: String?): Bitmap {

        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(uri)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap

        val resizedBitmap = Bitmap.createScaledBitmap(
            bitmap, 100, 100, true
        );

        return resizedBitmap
    }

    @Composable
    private fun LegacyBlurImage(
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

    fun Bitmap.convertToSoftwareBitmap(): Bitmap {
        val softwareBitmap = this.copy(Bitmap.Config.ARGB_8888, false)
        this.recycle()
        return softwareBitmap
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

    @Composable
    fun MyTopAppBar(
        onBackClick: () -> Unit = {},
        onEditClick: (Float) -> Unit,
        isBlurred: Boolean = false
    ) {
        TopAppBar(title = { Text(text = "Details") },
            navigationIcon = {
                IconButton(onClick = { onBackClick }) {
                    Icon(Icons.Filled.ArrowBack, null)
                }
            },
            actions = {
                IconButton(onClick = {
                    if (isBlurred)
                        onEditClick.invoke(0.1f)
                    else onEditClick.invoke(
                        0.1f
                    )
                }) {
                    Icon(Icons.Default.Edit, null)
                }
            })
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        UnsplashTheme {
            GetImagesScreen(vm = PhotoViewModel()) {

            }
        }
    }
}