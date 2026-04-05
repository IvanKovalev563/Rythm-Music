package com.example.rythmmusic

data class ClassSong (
    val title: String,
    val author: String,
    val coverImg: String?,
    var favorite: Boolean,
    val path: String
)