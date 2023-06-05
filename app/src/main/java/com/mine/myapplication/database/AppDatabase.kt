package com.mine.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mine.myapplication.dao.PhotoDao
import com.mine.myapplication.model.PhotoEntity

@Database(entities = [(PhotoEntity::class)], version = 4)
abstract class ImageRoomDatabase: RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {

        private var INSTANCE: ImageRoomDatabase? = null

        fun getInstance(context: Context): ImageRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ImageRoomDatabase::class.java,
                        "images_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}