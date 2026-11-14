package com.example.a21101373_juanalvarado_pc2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AsteroidDao {

    @Query(
        """SELECT * FROM asteroids_table
           WHERE approachDate >= :fromDate
           ORDER BY approachDate DESC, approachEpochMillis DESC, name ASC"""
    )
    abstract fun observeAll(fromDate: String): Flow<List<AsteroidEntity>>

    @Query(
        """SELECT * FROM asteroids_table
           WHERE approachDate >= :fromDate AND isHazardous = 1
           ORDER BY approachDate DESC, approachEpochMillis DESC, name ASC"""
    )
    abstract fun observeHazardous(fromDate: String): Flow<List<AsteroidEntity>>

    @Query(
        """SELECT * FROM asteroids_table
           WHERE isFavorite = 1
           ORDER BY approachDate DESC, approachEpochMillis DESC, name ASC"""
    )
    abstract fun observeFavorites(): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids_table WHERE id = :id LIMIT 1")
    abstract fun observeById(id: String): Flow<AsteroidEntity?>

    @Query("UPDATE asteroids_table SET isFavorite = :favorite WHERE id = :id")
    abstract suspend fun setFavorite(id: String, favorite: Boolean)

    @Query("SELECT id FROM asteroids_table WHERE isFavorite = 1")
    abstract suspend fun getFavoriteIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(items: List<AsteroidEntity>)

    @Query("DELETE FROM asteroids_table WHERE isFavorite = 0")
    abstract suspend fun deleteNonFavorites()

    @Transaction
    open suspend fun replaceKeepingFavorites(items: List<AsteroidEntity>) {
        val favoriteIds = getFavoriteIds().toSet()
        deleteNonFavorites()
        insertAll(items.map { it.copy(isFavorite = it.id in favoriteIds) })
    }
}
