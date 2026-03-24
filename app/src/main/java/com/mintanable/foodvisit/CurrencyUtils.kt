package com.mintanable.foodvisit

import java.util.Locale

/**
 * Maps an ISO 3166-1 alpha-2 country code (stored in [com.mintanable.core.model.RestaurantInfo.currency]
 * by [com.mintanable.core.data.mapper.PlaceMapper]) to the local currency symbol.
 *
 * Falls back to [java.util.Currency] for unlisted codes, and to "₹" when the code is absent
 * (the app defaults to Indian cities).
 */
fun currencySymbolFor(countryCode: String?): String {
    if (countryCode.isNullOrBlank()) return "₹"
    return when (countryCode.uppercase()) {
        "IN", "BT"                                          -> "₹"
        "US", "EC", "SV", "PA", "PR", "GU", "VI", "AS"    -> "$"
        "GB"                                                -> "£"
        // Euro-zone
        "DE", "FR", "IT", "ES", "NL", "BE", "AT", "PT",
        "FI", "IE", "GR", "LU", "MT", "CY", "SK", "SI",
        "EE", "LV", "LT", "HR", "MC", "SM", "VA", "AD"    -> "€"
        "JP"                                                -> "¥"
        "CN"                                                -> "¥"
        "KR"                                                -> "₩"
        "AU"                                                -> "A$"
        "NZ"                                                -> "NZ$"
        "CA"                                                -> "CA$"
        "SG"                                                -> "S$"
        "HK"                                                -> "HK$"
        "MX"                                                -> "MX$"
        "BR"                                                -> "R$"
        "CH"                                                -> "Fr"
        "SE", "NO", "DK"                                    -> "kr"
        "RU"                                                -> "₽"
        "TH"                                                -> "฿"
        "MY"                                                -> "RM"
        "ID"                                                -> "Rp"
        "PH"                                                -> "₱"
        "PK", "LK", "NP"                                    -> "Rs"
        "BD"                                                -> "৳"
        "AE", "SA", "QA", "KW", "BH", "OM"                 -> "﷼"
        "TR"                                                -> "₺"
        "ZA"                                                -> "R"
        "NG"                                                -> "₦"
        "EG"                                                -> "E£"
        "KE", "TZ", "UG"                                    -> "KSh"
        "GH"                                                -> "₵"
        "IL"                                                -> "₪"
        "AR"                                                -> "AR$"
        "CL"                                                -> "CLP$"
        "CO"                                                -> "COL$"
        else -> try {
            java.util.Currency.getInstance(Locale("", countryCode)).symbol
        } catch (_: Exception) { "₹" }
    }
}
