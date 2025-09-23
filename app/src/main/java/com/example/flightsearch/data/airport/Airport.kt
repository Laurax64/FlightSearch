package com.example.flightsearch.data.airport

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class representing a row in the airport table of the flight_search database
 */
@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "iata_code")
    val iataCode: String = "iataCode",
    val name: String = "name",
    val passengers: Int = -1,
)

fun String.toFlagEmoji(): String {
    if (this.length != 2) return "✈️"
    return this.uppercase()
        .map { char -> 0x1F1E6 - 'A'.code + char.code }
        .joinToString("") { code -> String(Character.toChars(code)) }
}

val airportCountryMap = mapOf(
    "OPO" to "PT",
    "ARN" to "SE",
    "WAW" to "PL",
    "MRS" to "FR",
    "BGY" to "IT",
    "VIE" to "AT",
    "SVO" to "RU",
    "DUB" to "IE",
    "SOF" to "BG",
    "CPH" to "DK",
    "BRU" to "BE",
    "FCO" to "IT",
    "ATH" to "GR",
    "BCN" to "ES",
    "DUS" to "DE",
    "GVA" to "CH",
    "KEF" to "IS",
    "HER" to "GR",
    "BSL" to "CH",
    "AGP" to "ES",
    "HEL" to "FI",
    "HAM" to "DE",
    "AMS" to "NL",
    "CDG" to "FR",
    "MAD" to "ES",
    "FRA" to "DE",
    "TXL" to "DE",
    "MUC" to "DE",
    "ALC" to "ES",
    "ZRH" to "CH",
    "LYS" to "FR",
    "OSL" to "NO",
    "LIS" to "PT",
    "LAX" to "US",
    "LHR" to "GB"
)

fun Airport.getFlag(): String {
    val countryCode = airportCountryMap[this.iataCode]
    return countryCode?.toFlagEmoji() ?: "✈️"
}


