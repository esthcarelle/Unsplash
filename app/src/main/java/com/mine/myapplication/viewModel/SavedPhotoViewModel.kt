package com.mine.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mine.myapplication.ImageRoomDatabase
import com.mine.myapplication.model.PhotoEntity
import com.mine.myapplication.repository.PhotoRepository

class SavedPhotoViewModel(application: Application) : AndroidViewModel(application) {

    var images: LiveData<List<PhotoEntity>> = MutableLiveData()

    private val repository: PhotoRepository

    init {
        val productDb = ImageRoomDatabase.getInstance(application)
        val productDao = productDb.photoDao()
        repository = PhotoRepository(productDao)

        images = repository.images
    }
    fun saveImage(photoEntity: PhotoEntity){
        repository.insertImage(photoEntity)
    }
}