package com.mine.myapplication

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoRepository(private val photoDao: PhotoDao) {
    val images : LiveData<List<PhotoEntity>> = photoDao.getAllSavedImages()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertImage(image: PhotoEntity) {
        coroutineScope.launch(Dispatchers.IO) {
            photoDao.insertImage(image)
        }
    }
}