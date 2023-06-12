package com.mine.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "imageId")
    var id: Int = 0,
    var url: String = "",
    var imageState: String = "",
    var offSetY: Float = 0f,
    var offSetX: Float = 0f,
    var scale: Float = 1f,
    var rotation: Float = 0f,
    var isBlurred: Boolean = false,
    var isFramed: Boolean = false
)

