package com.mine.myapplication

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotoDao {
    @Insert
    fun insertImage(photoEntity: PhotoEntity)

    @Query("SELECT * FROM images")
    fun getAllSavedImages(): LiveData<List<PhotoEntity>>
}