package com.example.a21101373_juanalvarado_pc2.data.repository

import com.example.a21101373_juanalvarado_pc2.BuildConfig
import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidDao
import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidEntity
import com.example.a21101373_juanalvarado_pc2.data.mapper.toEntities
import com.example.a21101373_juanalvarado_pc2.data.remote.NasaApiService
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AsteroidRepository(
    private val api: NasaApiService,
    private val dao: AsteroidDao
) {

    private fun windowStart(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        return dateFormat.format(calendar.time)
    }

    private fun windowEnd(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return dateFormat.format(Calendar.getInstance().time)
    }

    fun observeAsteroids(onlyHazardous: Boolean): Flow<List<AsteroidEntity>> {
        val from = windowStart()
        return if (onlyHazardous) dao.observeHazardous(from) else dao.observeAll(from)
    }

    fun observeFavorites(): Flow<List<AsteroidEntity>> = dao.observeFavorites()

    fun observeAsteroid(id: String): Flow<AsteroidEntity?> = dao.observeById(id)

    suspend fun setFavorite(id: String, favorite: Boolean) = dao.setFavorite(id, favorite)

    suspend fun refresh(): Result<Unit> = runCatching {
        val start = windowStart()
        val end = windowEnd()
        val apiKey = BuildConfig.NASA_API_KEY.ifBlank { "DEMO_KEY" }
        val response = api.getAsteroidFeed(startDate = start, endDate = end, apiKey = apiKey)
        dao.replaceKeepingFavorites(response.toEntities())
    }
}
