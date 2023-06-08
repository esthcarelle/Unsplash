package com.mine.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "imageId")
    var id: Int = 0,
    @ColumnInfo(name = "url")
    var url: String = "",
    @ColumnInfo(name = "imageState")
    var imageState: String = "",
    @ColumnInfo(name = "offSetY")
    var offSetY: Float = 0f,
    @ColumnInfo(name = "offSetX")
    var offSetX: Float = 0f,
    @ColumnInfo(name = "scale")
    var scale: Float = 1f,
    @ColumnInfo(name = "rotation")
    var rotation: Float = 0f
)

