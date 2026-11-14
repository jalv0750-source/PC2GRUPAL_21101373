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
    return ApodEntity(
        date = date.orEmpty(),
        title = if (title.isNullOrBlank()) "Sin título" else title,
        explanation = if (explanation.isNullOrBlank()) "Sin descripción disponible." else explanation,
        url = url.orEmpty(),
        hdurl = hdurl,
        mediaType = mediaType ?: "image",
        copyright = copyright,
        isFavorite = isFavorite
    )
}
