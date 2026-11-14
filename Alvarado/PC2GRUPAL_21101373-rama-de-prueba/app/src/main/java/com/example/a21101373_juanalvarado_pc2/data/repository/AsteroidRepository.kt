package com.example.a21101373_juanalvarado_pc2.data.repository

import com.example.a21101373_juanalvarado_pc2.BuildConfig
import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidDao
import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidEntity
import com.example.a21101373_juanalvarado_pc2.data.remote.NasaApiService
import com.example.a21101373_juanalvarado_pc2.data.remote.NeoFeedResponse
import com.example.a21101373_juanalvarado_pc2.data.remote.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AsteroidRepository(
    private val asteroidDao: AsteroidDao,
    private val nasaApiService: NasaApiService
) {

    val allAsteroids: Flow<List<AsteroidEntity>> = asteroidDao.getAllAsteroids()

    val favoriteAsteroids: Flow<List<AsteroidEntity>> = asteroidDao.getFavoriteAsteroids()

    suspend fun fetchAsteroidsFromApi(): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val (startDate, endDate) = calculateLast7DaysRange()
            val apiKey = BuildConfig.NASA_API_KEY.ifBlank { "DEMO_KEY" }

            var response: NeoFeedResponse? = null

            try {
                response = nasaApiService.getAsteroidFeed(
                    apiKey = apiKey,
                    startDate = startDate,
                    endDate = endDate
                )
            } catch (e: Exception) {
                if (isNetworkOffline(e)) throw e
            }

            if (response?.nearEarthObjects.isNullOrEmpty()) {
                try {
                    val (fbStart, fbEnd) = Pair("2024-11-08", "2024-11-14")
                    response = nasaApiService.getAsteroidFeed(
                        apiKey = apiKey,
                        startDate = fbStart,
                        endDate = fbEnd
                    )
                } catch (e: Exception) {
                    if (isNetworkOffline(e)) throw e
                }
            }

            val existingFavoriteIds = asteroidDao.getFavoriteIds().toSet()

            val dtoList = response?.nearEarthObjects?.values?.flatten() ?: emptyList()

            val entities = dtoList.mapNotNull { dto ->
                dto.id?.let { id ->
                    val isFav = existingFavoriteIds.contains(id)
                    dto.toEntity(isFavorite = isFav)
                }
            }.distinctBy { it.id }

            if (entities.isNotEmpty()) {
                asteroidDao.insertAsteroids(entities)
            } else {
                seedInitialDataIfEmpty()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            seedInitialDataIfEmpty()
            Result.failure(e)
        }
    }

    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        asteroidDao.updateFavoriteStatus(id, isFavorite)
    }

    private fun isNetworkOffline(e: Throwable): Boolean {
        return e is java.net.UnknownHostException ||
                e is java.net.ConnectException ||
                e is java.net.SocketTimeoutException ||
                e.cause is java.net.UnknownHostException ||
                e.cause is java.net.ConnectException
    }

    private suspend fun seedInitialDataIfEmpty() {
        if (asteroidDao.getAsteroidCount() == 0) {
            val sampleAsteroids = listOf(
                AsteroidEntity(
                    id = "3542519",
                    name = "(2010 PK9)",
                    closeApproachDate = "2024-11-14",
                    estimatedDiameterMinMeters = 116.0,
                    estimatedDiameterMaxMeters = 259.4,
                    relativeVelocityKmH = 54720.5,
                    missDistanceKm = 6400000.0,
                    isPotentiallyHazardous = true,
                    isFavorite = false
                ),
                AsteroidEntity(
                    id = "3726712",
                    name = "(2015 BQ514)",
                    closeApproachDate = "2024-11-13",
                    estimatedDiameterMinMeters = 45.2,
                    estimatedDiameterMaxMeters = 101.1,
                    relativeVelocityKmH = 31200.0,
                    missDistanceKm = 12500000.0,
                    isPotentiallyHazardous = false,
                    isFavorite = false
                ),
                AsteroidEntity(
                    id = "3841920",
                    name = "(2019 FA)",
                    closeApproachDate = "2024-11-12",
                    estimatedDiameterMinMeters = 88.5,
                    estimatedDiameterMaxMeters = 198.0,
                    relativeVelocityKmH = 48900.8,
                    missDistanceKm = 4800000.0,
                    isPotentiallyHazardous = true,
                    isFavorite = false
                ),
                AsteroidEntity(
                    id = "2001566",
                    name = "1566 Icarus (1949 MA)",
                    closeApproachDate = "2024-11-11",
                    estimatedDiameterMinMeters = 900.0,
                    estimatedDiameterMaxMeters = 1400.0,
                    relativeVelocityKmH = 104500.0,
                    missDistanceKm = 18200000.0,
                    isPotentiallyHazardous = true,
                    isFavorite = false
                ),
                AsteroidEntity(
                    id = "3651092",
                    name = "(2013 UG1)",
                    closeApproachDate = "2024-11-10",
                    estimatedDiameterMinMeters = 32.0,
                    estimatedDiameterMaxMeters = 71.5,
                    relativeVelocityKmH = 22100.4,
                    missDistanceKm = 9400000.0,
                    isPotentiallyHazardous = false,
                    isFavorite = false
                ),
                AsteroidEntity(
                    id = "3791204",
                    name = "(2017 WD)",
                    closeApproachDate = "2024-11-09",
                    estimatedDiameterMinMeters = 18.2,
                    estimatedDiameterMaxMeters = 40.7,
                    relativeVelocityKmH = 19800.0,
                    missDistanceKm = 3100000.0,
                    isPotentiallyHazardous = false,
                    isFavorite = false
                ),
                AsteroidEntity(
                    id = "3549210",
                    name = "(2010 RS14)",
                    closeApproachDate = "2024-11-08",
                    estimatedDiameterMinMeters = 150.0,
                    estimatedDiameterMaxMeters = 335.8,
                    relativeVelocityKmH = 61400.2,
                    missDistanceKm = 7900000.0,
                    isPotentiallyHazardous = true,
                    isFavorite = false
                )
            )
            asteroidDao.insertAsteroids(sampleAsteroids)
        }
    }

    private fun calculateLast7DaysRange(): Pair<String, String> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()
        if (calendar.get(Calendar.YEAR) > 2024) {
            calendar.set(Calendar.YEAR, 2024)
        }
        val endDate = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        val startDate = dateFormat.format(calendar.time)
        return Pair(startDate, endDate)
    }
}
