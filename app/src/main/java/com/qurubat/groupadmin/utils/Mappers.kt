package com.qurubat.groupadmin.utils

object Mappers {

    val countryNameMapper = mutableMapOf(
        "المغرب" to "مغربية",
        "الجزائر" to "جزائرية",
        "مصر" to "مصرية",
        "العراق" to "عراقية",
        "عمان" to "عمانية",
        "السعودية" to "سعودية",
        "تونس" to "تونسية",
        "الإمارات" to "امراتية",
        "اليمن" to "يمنية",
        "البحرين" to "بحرينية",
        "قطر" to "قطرية",
        "الكويت" to "كويتية",
        "الأردن" to "أردنية",
        "أروبا" to "أروبية",

        "أمريكا" to "أمريكية",
        "سوريا" to "سورية",
        "ليبيا" to "ليبية",
        "لبنان" to "لبنانية",

        "تركيا" to "تركية",
        "سودان" to "سودانية",
        "فرنسا" to "فرنسية",
        "تتارستان" to "تتارستانية",
    )

    fun getCountryName(countryName: String): String {
        return countryNameMapper[countryName] ?: ""

    }
}
