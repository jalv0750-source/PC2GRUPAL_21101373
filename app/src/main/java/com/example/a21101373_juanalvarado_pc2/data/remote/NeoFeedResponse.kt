package com.example.a21101373_juanalvarado_pc2.data.remote

import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidEntity
import com.google.gson.annotations.SerializedName

data class NeoFeedResponse(
    @SerializedName("element_count")
    val elementCount: Int? = null,
    @SerializedName("near_earth_objects")
    val nearEarthObjects: Map<String, List<AsteroidDto>>? = null
)

data class AsteroidDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean? = null,
    @SerializedName("estimated_diameter")
    val estimatedDiameter: EstimatedDiameterContainerDto? = null,
    @SerializedName("close_approach_data")
    val closeApproachData: List<CloseApproachDataDto>? = null
)

data class EstimatedDiameterContainerDto(
    @SerializedName("meters")
    val meters: EstimatedDiameterMetersDto? = null
)

data class EstimatedDiameterMetersDto(
    @SerializedName("estimated_diameter_min")
    val minMeters: Double? = null,
    @SerializedName("estimated_diameter_max")
    val maxMeters: Double? = null
)

data class CloseApproachDataDto(
    @SerializedName("close_approach_date")
    val closeApproachDate: String? = null,
    @SerializedName("relative_velocity")
    val relativeVelocity: RelativeVelocityDto? = null,
    @SerializedName("miss_distance")
    val missDistance: MissDistanceDto? = null
)

data class RelativeVelocityDto(
    @SerializedName("kilometers_per_hour")
    val kilometersPerHour: String? = null
)

data class MissDistanceDto(
    @SerializedName("kilometers")
    val kilometers: String? = null
)

fun AsteroidDto.toEntity(isFavorite: Boolean = false): AsteroidEntity? {
    val asteroidId = id?.trim()?.ifEmpty { null } ?: return null
    val cleanName = name?.trim()?.ifEmpty { null } ?: "Asteroide sin nombre"
    val isHazardous = isPotentiallyHazardous ?: false

    val minMeters = estimatedDiameter?.meters?.minMeters ?: 0.0
    val maxMeters = estimatedDiameter?.meters?.maxMeters ?: 0.0

    val closeApproach = closeApproachData?.firstOrNull()
    val approachDate = closeApproach?.closeApproachDate?.trim()?.ifEmpty { null } ?: "Sin fecha"
    val velocityKmH = closeApproach?.relativeVelocity?.kilometersPerHour?.toDoubleOrNull() ?: 0.0
    val missDistanceKm = closeApproach?.missDistance?.kilometers?.toDoubleOrNull() ?: 0.0

    return AsteroidEntity(
        id = asteroidId,
        name = cleanName,
        closeApproachDate = approachDate,
        estimatedDiameterMinMeters = minMeters,
        estimatedDiameterMaxMeters = maxMeters,
        relativeVelocityKmH = velocityKmH,
        missDistanceKm = missDistanceKm,
        isPotentiallyHazardous = isHazardous,
        isFavorite = isFavorite
    )
}
