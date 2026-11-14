package com.example.a21101373_juanalvarado_pc2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroids(asteroids: List<AsteroidEntity>)

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate DESC, name ASC")
    fun getAllAsteroids(): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids WHERE isFavorite = 1 ORDER BY closeApproachDate DESC, name ASC")
    fun getFavoriteAsteroids(): Flow<List<AsteroidEntity>>

    @Query("UPDATE asteroids SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("SELECT id FROM asteroids WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<String>

    @Query("SELECT COUNT(*) FROM asteroids")
    suspend fun getAsteroidCount(): Int
}
