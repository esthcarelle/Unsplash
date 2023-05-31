package com.mine.myapplication

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
class PhotoEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "imageId")
    var id:Int = 0

    @ColumnInfo(name = "url")
    var url: String = ""

    constructor(){}

    constructor(url:String){
        this.url = url
    }
}