package jp.ac.it_college.std.s23005.my_weather_sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.it_college.std.s23005.my_weather_sample.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val WEATHER_FORECAST_URL =
            "https://api.openweathermap.org/data/2.5/forecast?lang=ja&units=metric"
        private const val APP_ID = BuildConfig.WEATHER_API_KEY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lvCityList.apply {
            adapter = CityListAdapter(City.list, ::receiveWeatherInfo)
            val manager = LinearLayoutManager(this@MainActivity)
            layoutManager = manager
            addItemDecoration(DividerItemDecoration(this@MainActivity, manager.orientation))
        }
    }

    private fun receiveWeatherInfo(city: City) {
        lifecycleScope.launch {
            val url = "$WEATHER_FORECAST_URL&q=${city.q}&appid=$APP_ID"
            val forecast = fetchWeatherForecast(url)
            forecast?.let {
                displayWeatherForecast(it)
            }
        }
    }

    private suspend fun fetchWeatherForecast(url: String): ForecastResponse? {
        return withContext(Dispatchers.IO) {
            val json = Json { ignoreUnknownKeys = true }
            try {
                val result = URL(url).readText()
                json.decodeFromString(ForecastResponse.serializer(), result)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun displayWeatherForecast(forecast: ForecastResponse) {
        val forecastDetails = StringBuilder()

        // 最初の5つの予報エントリ（15時間分）を表示
        forecast.list.take(5).forEach { forecastEntry ->
            forecastDetails.append("日時: ${forecastEntry.dt_txt}\n")
            forecastDetails.append("気温: ${forecastEntry.main.temp}°C\n")
            forecastDetails.append("説明: ${forecastEntry.weather[0].description}\n")
            forecastDetails.append("\n")
        }

        binding.tvWeatherDesc.text = forecastDetails.toString()
    }
}
