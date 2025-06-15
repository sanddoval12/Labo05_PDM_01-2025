package com.falconteam.laboratorio_5.data.database.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.falconteam.laboratorio_5.data.database.Entity.UserTokenEntity

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(tokenEntity: UserTokenEntity)

    @Query("SELECT * FROM table_user_token LIMIT 1")
    suspend fun getToken(): UserTokenEntity?

    @Query("DELETE FROM table_user_token")
    suspend fun deleteAll()
}