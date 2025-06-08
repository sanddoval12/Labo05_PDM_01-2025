package com.falconteam.laboratorio_5.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.falconteam.laboratorio_5.data.database.daos.PostDao
import com.falconteam.laboratorio_5.data.database.entity.Post

@Database(entities = [Post::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun postDao(): PostDao
}