package com.mine.myapplication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.mine.myapplication.model.PhotoEntity
import com.mine.myapplication.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SliderView(state: PagerState, imageUrls: List<PhotoEntity>) {

    val imageUrl =
        remember { mutableStateOf("") }

    HorizontalPager(
        state = state,
        count = 3, modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
    ) { page ->
        imageUrls[page].url.also { imageUrl.value = it }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomCenter) {

                val painter = rememberImagePainter(data = imageUrl.value, builder = {
                    placeholder(R.drawable.custom_image)
                    scale(Scale.FILL)
                })
                Image(
                    painter = painter,
                    contentDescription = "",
                    androidx.compose.ui.Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
