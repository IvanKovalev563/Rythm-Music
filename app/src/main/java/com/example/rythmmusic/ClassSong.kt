package com.example.rythmmusic

data class ClassSong (
    val title: String,
    val author: String,
    val coverImg: Int,
    var favorite: Boolean = false
)