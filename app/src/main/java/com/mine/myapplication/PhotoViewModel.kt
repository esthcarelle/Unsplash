package com.mine.myapplication

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {
    private val _todoList = mutableStateListOf<ResponseItem>()
    var errorMessage: String by mutableStateOf("")
    val todoList: List<ResponseItem>
        get() = _todoList

    init {
        getTodoList()
    }

    private fun getTodoList() {
        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                _todoList.clear()
                val list = apiService.getPhotos("Client-ID dixtqIxMkkn0gBKvye_yGfKHH3dUxemwT_QwBFwYW04", 20)

                list.forEach {
                    if (it != null) {
                        Log.e(TAG, "getTodoList: "+it )

                        _todoList.add(it)
                    }
                }

            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}