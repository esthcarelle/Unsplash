package com.mine.myapplication.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.mine.myapplication.viewModel.SavedPhotoViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SliderView(state: PagerState, url:String, viewModel: SavedPhotoViewModel) {
    val imageUrls by viewModel.images.observeAsState(listOf())

    val imageUrl =
        remember { mutableStateOf("") }

    val position = imageUrls.indexOfFirst { it.url == url }

    LaunchedEffect(imageUrls) {
        if (imageUrls.isNotEmpty()) {
            // Scroll to the first image on first load
            state.scrollToPage(position)
        }
    }
    HorizontalPager(
        state = state,
        count = imageUrls.size, modifier = Modifier
            .height(400.dp)
            .fillMaxWidth()
    ) { page ->
        imageUrls[page].url.also { imageUrl.value = it }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomCenter) {
                SavedPhotoDetails(photoEntity = imageUrls[page])
            }
        }
    }
}
