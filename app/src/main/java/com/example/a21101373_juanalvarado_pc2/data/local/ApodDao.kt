package com.example.a21101373_juanalvarado_pc2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ApodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApods(apods: List<ApodEntity>)

    @Query("SELECT * FROM apods ORDER BY date DESC")
    fun getAllApods(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apods WHERE isFavorite = 1 ORDER BY date DESC")
    fun getFavoriteApods(): Flow<List<ApodEntity>>

    @Query("UPDATE apods SET isFavorite = :isFavorite WHERE date = :date")
    suspend fun updateFavoriteStatus(date: String, isFavorite: Boolean)

    @Query("SELECT date FROM apods WHERE isFavorite = 1")
    suspend fun getFavoriteDates(): List<String>
}
