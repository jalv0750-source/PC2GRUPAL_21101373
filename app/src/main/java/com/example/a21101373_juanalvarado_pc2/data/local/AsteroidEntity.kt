package com.example.a21101373_juanalvarado_pc2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroids_table")
data class AsteroidEntity(
    @PrimaryKey val id: String,
    val name: String,
    val approachDate: String,
    val approachEpochMillis: Long,
    val diameterMinM: Double?,
    val diameterMaxM: Double?,
    val velocityKmh: Double?,
    val missDistanceKm: Double?,
    val isHazardous: Boolean,
    val isFavorite: Boolean = false
)
