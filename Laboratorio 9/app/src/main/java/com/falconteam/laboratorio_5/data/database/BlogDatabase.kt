package com.falconteam.laboratorio_5.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.falconteam.laboratorio_5.data.database.Daos.AuthDao
import com.falconteam.laboratorio_5.data.database.Daos.PostDao
import com.falconteam.laboratorio_5.data.database.Entity.PostEntity
import com.falconteam.laboratorio_5.data.database.Entity.UserTokenEntity
import com.falconteam.laboratorio_5.data.remote.services.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    @TypeConverter
    fun fromString(value: String): List<Message> {
        val listType = object : TypeToken<List<Message>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Message>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
@Database(
    entities = [PostEntity::class, UserTokenEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun authDao(): AuthDao
}