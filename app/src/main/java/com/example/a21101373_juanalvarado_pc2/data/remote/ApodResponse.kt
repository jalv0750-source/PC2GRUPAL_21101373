package com.example.a21101373_juanalvarado_pc2.data.remote

import com.example.a21101373_juanalvarado_pc2.data.local.ApodEntity
import com.google.gson.annotations.SerializedName

data class ApodResponse(
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("explanation")
    val explanation: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("hdurl")
    val hdurl: String? = null,
    @SerializedName("media_type")
    val mediaType: String? = null,
    @SerializedName("copyright")
    val copyright: String? = null
)

fun ApodResponse.toEntity(isFavorite: Boolean = false): ApodEntity {
    val cleanTitle = title?.trim()?.ifEmpty { null } ?: "Sin título"
    val cleanUrl = url?.trim().orEmpty()
    val cleanHdUrl = hdurl?.trim()?.ifEmpty { null }
    val cleanExplanation = explanation?.trim()?.ifEmpty { null } ?: "Sin descripción disponible."
    val cleanCopyright = copyright?.trim()?.ifEmpty { null }
    val cleanMediaType = mediaType?.trim()?.ifEmpty { null } ?: "image"

    return ApodEntity(
        date = date?.trim().orEmpty(),
        title = cleanTitle,
        explanation = cleanExplanation,
        url = cleanUrl,
        hdurl = cleanHdUrl,
        mediaType = cleanMediaType,
        copyright = cleanCopyright,
        isFavorite = isFavorite
    )
}
