package com.example.a21101373_juanalvarado_pc2.data.remote

import com.google.gson.annotations.SerializedName

data class NeoFeedResponse(
    @SerializedName("near_earth_objects")
    val nearEarthObjects: Map<String, List<NeoDto>?>?
)

data class NeoDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("is_potentially_hazardous_asteroid") val isHazardous: Boolean?,
    @SerializedName("estimated_diameter") val estimatedDiameter: EstimatedDiameterDto?,
    @SerializedName("close_approach_data") val closeApproachData: List<CloseApproachDto>?
)

data class EstimatedDiameterDto(
    @SerializedName("meters") val meters: DiameterRangeDto?
)

data class DiameterRangeDto(
    @SerializedName("estimated_diameter_min") val min: Double?,
    @SerializedName("estimated_diameter_max") val max: Double?
)

data class CloseApproachDto(
    @SerializedName("close_approach_date") val date: String?,
    @SerializedName("epoch_date_close_approach") val epochMillis: Long?,
    @SerializedName("relative_velocity") val relativeVelocity: RelativeVelocityDto?,
    @SerializedName("miss_distance") val missDistance: MissDistanceDto?
)

data class RelativeVelocityDto(
    @SerializedName("kilometers_per_hour") val kilometersPerHour: String?
)

data class MissDistanceDto(
    @SerializedName("kilometers") val kilometers: String?
)
