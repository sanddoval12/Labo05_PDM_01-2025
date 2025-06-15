package com.falconteam.laboratorio_5.data.database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "table_user_token")
data class UserTokenEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val user: String,
    val token: String
)