package com.mine.myapplication

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoRepository(private val photoDao: PhotoDao) {
    val images : LiveData<List<PhotoEntity>> = photoDao.getAllSavedImages()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertImage(image: PhotoEntity) {
        coroutineScope.launch(Dispatchers.IO) {
           val res = photoDao.insertImage(image)
            Log.e(TAG, "insertImage: "+res )
        }
    }
}