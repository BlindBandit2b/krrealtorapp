package com.example.realtorapp

data class ApartmentInfo(
    val apartmentNumber: Int = 0,
    val floor: Int = 0,
    val area: Double = 0.0,
    val numRooms: Int = 0,
    val bathroomType: String = "",
    val status: String = "",
    val houseAddress: String = ""
)