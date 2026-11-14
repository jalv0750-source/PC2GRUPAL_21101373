package com.example.a21101373_juanalvarado_pc2.data.mapper

import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidEntity
import com.example.a21101373_juanalvarado_pc2.data.remote.NeoDto
import com.example.a21101373_juanalvarado_pc2.data.remote.NeoFeedResponse

fun NeoFeedResponse.toEntities(): List<AsteroidEntity> {
    val grouped = nearEarthObjects ?: return emptyList()

    return grouped.flatMap { (dateKey, list) ->
        (list ?: emptyList()).mapNotNull { dto -> dto.toEntity(dateKey) }
    }
        .groupBy { it.id }
        .map { (_, versions) -> versions.maxWith(compareBy({ it.approachDate }, { it.approachEpochMillis })) }
}

fun NeoDto.toEntity(dateKey: String): AsteroidEntity? {
    val safeId = id?.takeIf { it.isNotBlank() } ?: return null

    val approach = closeApproachData?.firstOrNull { it?.date == dateKey }
        ?: closeApproachData?.firstOrNull()

    val meters = estimatedDiameter?.meters

    return AsteroidEntity(
        id = safeId,
        name = name?.takeIf { it.isNotBlank() } ?: "Sin nombre",
        approachDate = approach?.date?.takeIf { it.isNotBlank() } ?: dateKey,
        approachEpochMillis = approach?.epochMillis ?: 0L,
        diameterMinM = meters?.min,
        diameterMaxM = meters?.max,
        velocityKmh = approach?.relativeVelocity?.kilometersPerHour.toDoubleSafe(),
        missDistanceKm = approach?.missDistance?.kilometers.toDoubleSafe(),
        isHazardous = isHazardous ?: false
    )
}

private fun String?.toDoubleSafe(): Double? = this?.trim()?.toDoubleOrNull()
