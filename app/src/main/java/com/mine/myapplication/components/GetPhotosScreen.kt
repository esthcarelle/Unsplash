package com.mine.myapplication.components

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mine.myapplication.viewModel.PhotoViewModel

@Composable
fun GetImagesScreen(onNavigateToDetailsScreen: (String) -> Unit) {
    val vm = PhotoViewModel()
    val scrollState = rememberLazyGridState()
    val endOfListReached by remember {
        derivedStateOf {
            scrollState.isScrolledToEnd()
        }
    }

    // act when end of list reached
    LaunchedEffect(endOfListReached) {
        // do your stuff
        vm.getImages()
    }

    // act when end of list reached

    LazyVerticalGrid(state = scrollState,modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2)) {
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
                        .data(photo.urls?.full)
                        .build(),
                    modifier = Modifier,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }
        }
//        if(scrollState.isScrolledToEnd())
//            Log.e(TAG, "GetImagesScreen: " )
    }
}
fun LazyGridState.isScrolledToEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1


