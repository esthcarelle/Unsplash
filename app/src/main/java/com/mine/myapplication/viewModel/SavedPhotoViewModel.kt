package com.mine.myapplication.viewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
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

    fun saveImage(photoEntity: PhotoEntity) {
        repository.insertImage(photoEntity)
    }

    suspend fun uriToBitmap(context: Context, uri: String?): Bitmap {

        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(uri)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap

        return Bitmap.createScaledBitmap(
            bitmap, 100, 100, true
        )
    }
}