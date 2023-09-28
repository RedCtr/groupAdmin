package com.qurubat.groupadmin.models

data class Group(
    val groupName: String = "",
    val groupImage: String = "",
    val groupLink: String = "",
    val genre: String = "",
    val countryImage: String = "",
    val countryName: String = "",
    val groupDescription: String = "",
    val groupImagePathName: String = "",

    ) {
    var id: String = ""
}