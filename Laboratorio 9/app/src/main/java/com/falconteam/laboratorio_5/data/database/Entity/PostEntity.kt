package com.falconteam.laboratorio_5.data.database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.falconteam.laboratorio_5.data.remote.services.Message
import java.util.UUID

@Entity(tableName = "table_post")
data class PostEntity(
    @PrimaryKey(autoGenerate = false) val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val author: String,
    val comments: List<Message>
)