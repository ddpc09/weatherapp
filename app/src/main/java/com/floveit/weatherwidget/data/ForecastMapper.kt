package com.floveit.weatherwidget.data


import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class ForecastMapper {

    private val hourFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun mapDaily(dto: ForecastResponse): List<DailyForecast> =
        dto.forecast.forecastday.map {
            DailyForecast(
                date = it.date,
                maxTempC = it.day.maxtemp_c,
                minTempC = it.day.mintemp_c,
                conditionCode = it.day.condition.code
            )
        }

    /**
     * Rolling next 24 hours from "now" in the *location's* timezone.
     * Falls back to system zone if tz_id is missing/unparseable.
     */
    fun mapHourly(dto: ForecastResponse): List<HourlyForecast> {
        val zone = runCatching { ZoneId.of(dto.location.tz_id) }
            .getOrElse { ZoneId.systemDefault() }

        // Truncate to the hour so we align with API “HH:00” buckets.
        val nowLocal = ZonedDateTime.now(zone).truncatedTo(ChronoUnit.HOURS)

        // Flatten all hours the API returned (day 0, day 1, ...),
        // filter those that are >= now, take 24.
        val allHours = dto.forecast.forecastday.flatMap { it.hour }

        return allHours.asSequence()
            .map { h ->
                val lt = LocalDateTime.parse(h.time, hourFmt) // local time string
                val zdt = lt.atZone(zone)
                h to zdt
            }
            .filter { (_, zdt) -> !zdt.isBefore(nowLocal) }
            .sortedBy { it.second }           // ensure chronological order
            .take(24)
            .map { (h, _) ->
                HourlyForecast(
                    time = h.time,               // keep original "yyyy-MM-dd HH:mm"
                    tempC = h.temp_c,
                    isDay = h.is_day == 1,
                    conditionCode = h.condition.code
                )
            }
            .toList()
    }
}