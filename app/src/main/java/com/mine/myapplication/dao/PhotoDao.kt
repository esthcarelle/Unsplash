package com.mine.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mine.myapplication.model.PhotoEntity

@Dao
interface PhotoDao {
    @Insert
    fun insertImage(photoEntity: PhotoEntity): Long

    @Query("SELECT * FROM images")
    fun getAllSavedImages(): LiveData<List<PhotoEntity>>
}