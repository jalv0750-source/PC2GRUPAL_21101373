package com.example.a21101373_juanalvarado_pc2.data.repository

import com.example.a21101373_juanalvarado_pc2.BuildConfig
import com.example.a21101373_juanalvarado_pc2.data.local.ApodDao
import com.example.a21101373_juanalvarado_pc2.data.local.ApodEntity
import com.example.a21101373_juanalvarado_pc2.data.remote.ApodResponse
import com.example.a21101373_juanalvarado_pc2.data.remote.NasaApiService
import com.example.a21101373_juanalvarado_pc2.data.remote.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ApodRepository(
    private val apodDao: ApodDao,
    private val nasaApiService: NasaApiService
) {

    val allApods: Flow<List<ApodEntity>> = apodDao.getAllApods()

    val favoriteApods: Flow<List<ApodEntity>> = apodDao.getFavoriteApods()

    suspend fun fetchApodsFromApi(): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val (startDate, endDate) = calculateLast7DaysRange()
            val apiKey = BuildConfig.NASA_API_KEY.ifBlank { "DEMO_KEY" }

            var responseList: List<ApodResponse>? = null

            try {
                responseList = nasaApiService.getApodImages(
                    apiKey = apiKey,
                    startDate = startDate,
                    endDate = endDate
                )
            } catch (e: Exception) {
                if (isNetworkOffline(e)) throw e
            }

            if (responseList.isNullOrEmpty()) {
                try {
                    responseList = nasaApiService.getApodImagesCount(
                        apiKey = apiKey,
                        count = 7
                    )
                } catch (e: Exception) {
                    if (isNetworkOffline(e)) throw e
                }
            }

            if (responseList.isNullOrEmpty()) {
                try {
                    val (fbStart, fbEnd) = Pair("2024-11-08", "2024-11-14")
                    responseList = nasaApiService.getApodImages(
                        apiKey = apiKey,
                        startDate = fbStart,
                        endDate = fbEnd
                    )
                } catch (e: Exception) {
                    if (isNetworkOffline(e)) throw e
                }
            }

            val existingFavoriteDates = apodDao.getFavoriteDates().toSet()

            val entities = responseList?.mapNotNull { response ->
                if (response.date.isNullOrBlank()) null
                else {
                    val isFav = existingFavoriteDates.contains(response.date)
                    response.toEntity(isFavorite = isFav)
                }
            }.orEmpty()

            if (entities.isNotEmpty()) {
                apodDao.insertApods(entities)
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

    suspend fun updateFavoriteStatus(date: String, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        apodDao.updateFavoriteStatus(date, isFavorite)
    }

    private fun isNetworkOffline(e: Throwable): Boolean {
        return e is java.net.UnknownHostException ||
                e is java.net.ConnectException ||
                e is java.net.SocketTimeoutException ||
                e.cause is java.net.UnknownHostException ||
                e.cause is java.net.ConnectException
    }

    private suspend fun seedInitialDataIfEmpty() {
        if (apodDao.getApodCount() == 0) {
            val sampleApods = listOf(
                ApodEntity(
                    date = "2026-11-14",
                    title = "Apollo 12 and Surveyor 3",
                    explanation = "Put on your red/blue glasses and gaze across the western Ocean of Storms on the surface of the Moon. The 3D anaglyph shows Apollo 12 astronaut Pete Conrad visiting the Surveyor 3 spacecraft in November of 1969.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/KF-ApAn48-7133-4.jpg?w=2181&h=2200&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/KF-ApAn48-7133-4.jpg?w=2181&h=2200&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Apollo 12, Alan Bean - Stereo Image Copyright: Kevin Frank",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-13",
                    title = "NGC 206 and the Star Clouds of Andromeda",
                    explanation = "The large stellar association cataloged as NGC 206 is nestled within the dusty arms of the neighboring Andromeda galaxy along with the galaxy's pinkish star-forming regions.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/Andromeda_closeupCruz4096.jpg?w=4096&h=2902&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/Andromeda_closeupCruz4096.jpg?w=4096&h=2902&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "David Cruz",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-12",
                    title = "Moon, Venus and Spica Meet",
                    explanation = "Not all those who wander are lost. A night sky example is that planets wander, and in fact the very word 'planet' comes from 'wandering star' in Greek.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/conjunction-bubak.jpg?w=1638&h=2048&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/conjunction-bubak.jpg?w=1638&h=2048&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Włodzimierz Bubak",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-11",
                    title = "NGC 4449: Small Galaxy, Big Impact",
                    explanation = "Don’t judge a galaxy by its size. Our home galaxy, the Milky Way, is about 100,000 light years across. This huge expanse of space contains hundreds of billions of stars!",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/missions/hubble/galaxies/irregular/NGC%204449.jpg?w=2048&h=1320&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/missions/hubble/galaxies/irregular/NGC%204449.jpg?w=2048&h=1320&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "NASA, ESA, A. Aloisi (STScI/ESA), and the Hubble Heritage Collaboration",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-10",
                    title = "Saturn Behind the Moon",
                    explanation = "Sometimes Saturn disappears. It doesn't really go away, of course -- it just disappears from our view when Earth's Moon moves in front.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/Moonsaturn6_Ortu_2124.jpg?w=2124&h=1582&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/Moonsaturn6_Ortu_2124.jpg?w=2124&h=1582&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Roberto Ortu",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-09",
                    title = "Rigel and the Witch Head Nebula",
                    explanation = "Does the blue nebula look like the head of a witch? The nebula is known popularly as the Witch Head Nebula because its shape resembles a witch's head.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/WitchHead_Czerski_3000.jpg?w=3000&h=1990&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/WitchHead_Czerski_3000.jpg?w=3000&h=1990&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Piotr Czerski",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-08",
                    title = "Major Fireball Meteor",
                    explanation = "The sky glows with soft pinkish colors of fading twilight in this serendipitous mountaintop vista. Taken in subfreezing temperatures on a mountain pass.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/meteor2017_Taylor_1200.jpg?w=0&h=0&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/meteor2017_Taylor_1200.jpg?w=0&h=0&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Ollie Taylor",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-07",
                    title = "De Gerlache-Kocher Massif",
                    explanation = "De Gerlache-Kocher massif looms on the visible horizon in this close-up of the edge of the Moon. The formation rises about 4 km near the lunar south pole.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/GerlacheKocherMassif_Sans.jpg?w=1500&h=1022&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/GerlacheKocherMassif_Sans.jpg?w=1500&h=1022&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Alejandro Sanz",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-06",
                    title = "A Farewell to Swift",
                    explanation = "It is time to say goodbye to NASA's Neil Gehrels Swift Observatory. After nearly 22 years of service, the satellite re-entered Earth's atmosphere on November 4.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/Swift_Millionth_Image_Mosaic.jpg?w=5252&h=3744&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/Swift_Millionth_Image_Mosaic.jpg?w=5252&h=3744&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "NASA / Swift and AndreaMosaic",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-05",
                    title = "Enceladus in Silhouette",
                    explanation = "One of our Solar System's most tantalizing worlds, Enceladus is backlit by the Sun in this Cassini spacecraft image from November 1, 2009.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/PIA17144EnceladusPandora.png?w=982&h=575&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/PIA17144EnceladusPandora.png?w=982&h=575&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Cassini Imaging Team, SSI, JPL, ESA, NASA",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-04",
                    title = "The Not-So-Hidden Galaxy, IC 342",
                    explanation = "“I see” IC 342, the Hidden Galaxy! Today’s image peers through the plane of the Milky Way, through all its gas, dust and stars, to unveil this spiral galaxy.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/ic342.png?w=4144&h=2822&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/ic342.png?w=4144&h=2822&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Artem Mironov",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-03",
                    title = "Comet PanSTARRs and Rainbow Meteor",
                    explanation = "Did this meteor come from that comet? Probably not. Although many meteors do originate from comets, there are thousands of comets visiting the inner Solar System.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/CometMeteor_Metais_1934.jpg?w=0&h=0&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/CometMeteor_Metais_1934.jpg?w=0&h=0&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Eddy Metais",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-02",
                    title = "A Long Cloud on Mars",
                    explanation = "What’s causing this unusually long cloud on Mars? Known as the Arsia Mons Elongated Cloud (AMEC) and first recorded by NASA’s Viking Orbiter 2 in 1977.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/MonsCloud_MarsExpress_1920.jpg?w=1920&h=1200&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/MonsCloud_MarsExpress_1920.jpg?w=1920&h=1200&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "ESA, DLR, FU Berlin; Processing & License: J. Cowart",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-11-01",
                    title = "Colors of the Moon",
                    explanation = "What color is the Moon? It depends on the night -- and the time. Outside of the Earth's atmosphere, the dark Moon appears a magnificent brown-tinged gray.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/MoonColors_Pace_960.jpg?w=960&h=991&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/november/MoonColors_Pace_960.jpg?w=960&h=991&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Marcella Giulia Pace",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-10-31",
                    title = "Night on a Spooky Planet",
                    explanation = "What spooky planet is this? Planet Earth on a dark and stormy night, of course. The ghostly scene was photographed at Hverir in Iceland.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/Hverir_Vetter_1300.jpg?w=1300&h=867&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/Hverir_Vetter_1300.jpg?w=1300&h=867&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Stéphane Vetter (Nuits sacrées)",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-10-30",
                    title = "Haunting the Cepheus Flare",
                    explanation = "Spooky shapes seem to haunt this dusty expanse, drifting through the night in the royal constellation Cepheus. The shapes are cosmic dust clouds in starlight.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/october/CepheusFlare_Tondinelli.jpg?w=6000&h=4076&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/october/CepheusFlare_Tondinelli.jpg?w=6000&h=4076&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Mirko Tondinelli",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-10-29",
                    title = "The Horns of Venus",
                    explanation = "That's no moon. But you could be forgiven for thinking it was a crescent Moon in the sky. It is a lesser known fact that Venus also has phases.",
                    url = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/october/venus_20261024a.jpg?w=744&h=744&fit=clip&crop=faces%2Cfocalpoint",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/october/venus_20261024a.jpg?w=744&h=744&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "image",
                    copyright = "Thierry Legault",
                    isFavorite = false
                ),
                ApodEntity(
                    date = "2026-10-28",
                    title = "The Mutual Occultation of Ganymede and Io",
                    explanation = "Jupiter is one of the least lonely planets in the Solar System. Ganymede and Io, two of Jupiter’s Galilean moons, met in the sky on October 20th, 2026.",
                    url = "https://assets.science.nasa.gov/content/dam/science/cds/apod/apod/2026/october/occultation.mp4",
                    hdurl = "https://assets.science.nasa.gov/dynamicimage/assets/science/cds/apod/apod/2026/october/occultation.jpeg?w=1600&h=1000&fit=clip&crop=faces%2Cfocalpoint",
                    mediaType = "video",
                    copyright = "Michael Wong",
                    isFavorite = false
                )
            )
            apodDao.insertApods(sampleApods)
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
