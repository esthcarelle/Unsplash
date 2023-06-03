package com.mine.myapplication.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import com.mine.myapplication.dao.PhotoDao
import com.mine.myapplication.model.PhotoEntity
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