package com.example.a21101373_juanalvarado_pc2.data.remote

import android.text.Html
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
    val cleanHdUrl = hdurl?.trim()?.ifEmpty { null }
    val rawUrl = url?.trim().orEmpty()

    val displayUrl = if (rawUrl.isBlank() || rawUrl.contains("image-article")) {
        cleanHdUrl ?: rawUrl
    } else {
        rawUrl
    }

    val rawExplanation = explanation?.trim()?.ifEmpty { null } ?: "Sin descripción disponible."
    val cleanExplanation = try {
        Html.fromHtml(rawExplanation, Html.FROM_HTML_MODE_LEGACY).toString().trim()
    } catch (e: Exception) {
        rawExplanation
    }

    val cleanCopyright = copyright?.trim()?.ifEmpty { null }?.let {
        try {
            Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString().trim()
        } catch (e: Exception) {
            it
        }
    }

    val cleanMediaType = mediaType?.trim()?.ifEmpty { null } ?: "image"

    return ApodEntity(
        date = date?.trim().orEmpty(),
        title = cleanTitle,
        explanation = cleanExplanation,
        url = displayUrl,
        hdurl = cleanHdUrl,
        mediaType = cleanMediaType,
        copyright = cleanCopyright,
        isFavorite = isFavorite
    )
}
