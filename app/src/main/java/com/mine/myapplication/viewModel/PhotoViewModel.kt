package com.mine.myapplication.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mine.myapplication.ResponseItem
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
                    "Client-ID As9FWYGaC73kkmWQcNV88XLOax02sC1PNPn1HZ_So_8",
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