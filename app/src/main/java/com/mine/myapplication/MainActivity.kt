package com.mine.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mine.myapplication.ui.theme.UnsplashTheme

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
        val clickCount = remember { mutableStateOf(0f) }


        Scaffold(topBar = {
            MyTopAppBar(onBackClick = onBackClick, onEditClick = { clickCount.value = it})
        }, content = {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(url)
//                        .build(),
//                    modifier = Modifier.blur(radius = 50.dp),
//                    contentDescription = "",
//                    contentScale = ContentScale.FillBounds
//                )
                clickCount.value
                val bitmap = BitmapFactory
                    .decodeResource(LocalContext.current.resources, R.drawable.custom_image)

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    LegacyBlurImage(bitmap, clickCount.value)
                } else {
                    BlurImage(
                        bitmap,
                        Modifier
                            .fillMaxSize()
                            .blur(radiusX = clickCount.value.dp, radiusY = clickCount.value.dp)
                    )
                }
            }


        })
    }

    @Composable
    private fun LegacyBlurImage(
        bitmap: Bitmap,
        blurRadio: Float = 25f,
        modifier: Modifier = Modifier.fillMaxSize()
    ) {

        val renderScript = RenderScript.create(LocalContext.current)
        val bitmapAlloc = Allocation.createFromBitmap(renderScript, bitmap)
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

    @Composable
    fun MyTopAppBar(onBackClick: () -> Unit = {}, onEditClick: (Float) -> Unit) {
        TopAppBar(title = { Text(text = "Details") },
            navigationIcon = {
                IconButton(onClick = { onBackClick }) {
                    Icon(Icons.Filled.ArrowBack, null)
                }
            },
            actions = {
                IconButton(onClick = { onEditClick.invoke(30f) }) {
                    Icon(Icons.Default.Edit, null)
                }
            })
    }

    fun Bitmap.applyGaussianBlur(context: Context, radius: Float): Bitmap {
        val rs = RenderScript.create(context)
        val input = Allocation.createFromBitmap(rs, this)
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setInput(input)
        script.setRadius(radius)
        script.forEach(output)
        output.copyTo(this)
        rs.destroy()
        return this
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