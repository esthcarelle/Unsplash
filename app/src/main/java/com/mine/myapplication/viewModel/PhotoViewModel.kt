package com.mine.myapplication.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mine.myapplication.model.ResponseItem
import com.mine.myapplication.utils.Constants.CLIENT_ID
import com.mine.myapplication.service.APIService
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {
    private val _imagesList = mutableStateListOf<ResponseItem>()
    private var errorMessage: String by mutableStateOf("")
    val imagesList: List<ResponseItem>
        get() = _imagesList

    init {
        getImages()
    }

    fun getImages() {
        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                val list = apiService.getPhotos(
                    CLIENT_ID,
                    20
                )


                list.forEach {
                    _imagesList.add(it)
                }
                Log.e(TAG, "getImages: "+_imagesList.size)

            } catch (e: Exception) {
                errorMessage = e.message.toString()
                Log.e(TAG, "getImages: "+errorMessage )
            }
        }
    }
}