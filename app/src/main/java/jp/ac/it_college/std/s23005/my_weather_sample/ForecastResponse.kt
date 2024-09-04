package jp.ac.it_college.std.s23005.my_weather_sample

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    @SerialName("list") val list: List<ForecastEntry>
)

@Serializable
data class ForecastEntry(
    @SerialName("dt_txt") val dt_txt: String,
    @SerialName("main") val main: MainData,
    @SerialName("weather") val weather: List<Weather>
)

@Serializable
data class MainData(
    @SerialName("temp") val temp: Double
)

@Serializable
data class Weather(
    @SerialName("description") val description: String
)
