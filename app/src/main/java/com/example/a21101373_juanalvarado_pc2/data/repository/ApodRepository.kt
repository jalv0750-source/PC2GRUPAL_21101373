package com.example.a21101373_juanalvarado_pc2.data.repository

import com.example.a21101373_juanalvarado_pc2.BuildConfig
import com.example.a21101373_juanalvarado_pc2.data.local.ApodDao
import com.example.a21101373_juanalvarado_pc2.data.local.ApodEntity
import com.example.a21101373_juanalvarado_pc2.data.remote.NasaApiService
import com.example.a21101373_juanalvarado_pc2.data.remote.toEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ApodRepository(
    private val apodDao: ApodDao,
    private val nasaApiService: NasaApiService
) {

    val allApods: Flow<List<ApodEntity>> = apodDao.getAllApods()

    val favoriteApods: Flow<List<ApodEntity>> = apodDao.getFavoriteApods()

    suspend fun fetchApodsFromApi(): Result<Unit> {
        return try {
            val (startDate, endDate) = calculateLast7DaysRange()
            val apiKey = BuildConfig.NASA_API_KEY.ifBlank { "DEMO_KEY" }

            val responseList = nasaApiService.getApodImages(
                apiKey = apiKey,
                startDate = startDate,
                endDate = endDate
            )

            val existingFavoriteDates = apodDao.getFavoriteDates().toSet()

            val entities = responseList.mapNotNull { response ->
                if (response.date.isNullOrBlank()) null
                else {
                    val isFav = existingFavoriteDates.contains(response.date)
                    response.toEntity(isFavorite = isFav)
                }
            }

            if (entities.isNotEmpty()) {
                apodDao.insertApods(entities)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun updateFavoriteStatus(date: String, isFavorite: Boolean) {
        apodDao.updateFavoriteStatus(date, isFavorite)
    }

    private fun calculateLast7DaysRange(): Pair<String, String> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()
        val endDate = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        val startDate = dateFormat.format(calendar.time)
        return Pair(startDate, endDate)
    }
}
