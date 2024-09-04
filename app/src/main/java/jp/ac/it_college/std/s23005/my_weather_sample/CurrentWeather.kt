package jp.ac.it_college.std.s23005.my_weather_sample

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather(
    @SerialName("coord") val coordinates: Coordinates,
    val weather: List<Weather>,
    @SerialName("name") val cityName: String
)

@Serializable
data class Coordinates(
    @SerialName("lon") val longitude: Double,
    @SerialName("lat") val latitude: Double,
)

