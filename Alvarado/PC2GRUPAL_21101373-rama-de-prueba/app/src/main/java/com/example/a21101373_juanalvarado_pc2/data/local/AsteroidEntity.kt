package com.example.a21101373_juanalvarado_pc2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroids")
data class AsteroidEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val closeApproachDate: String,
    val estimatedDiameterMinMeters: Double,
    val estimatedDiameterMaxMeters: Double,
    val relativeVelocityKmH: Double,
    val missDistanceKm: Double,
    val isPotentiallyHazardous: Boolean,
    val isFavorite: Boolean = false
)
