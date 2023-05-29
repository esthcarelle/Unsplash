package com.mine.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {
    private val _imagesList = mutableStateListOf<ResponseItem>()
    private var errorMessage: String by mutableStateOf("")
    val imagesList: List<ResponseItem>
        get() = _imagesList

    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                _imagesList.clear()
                val list = apiService.getPhotos(
                    "Client-ID dixtqIxMkkn0gBKvye_yGfKHH3dUxemwT_QwBFwYW04",
                    20
                )

                list.forEach {
                    _imagesList.add(it)
                }

            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}