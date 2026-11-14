package com.example.a21101373_juanalvarado_pc2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apods")
data class ApodEntity(
    @PrimaryKey
    val date: String,
    val title: String,
    val explanation: String,
    val url: String,
    val hdurl: String? = null,
    val mediaType: String,
    val copyright: String? = null,
    val isFavorite: Boolean = false
)
