package com.mine.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SavedPhotoViewModel(application: Application) : AndroidViewModel(application) {

    var images: LiveData<List<PhotoEntity>> = MutableLiveData()

    private val repository: PhotoRepository

    init {
        val productDb = ImageRoomDatabase.getInstance(application)
        val productDao = productDb.photoDao()
        repository = PhotoRepository(productDao)

        images = repository.images
    }
}