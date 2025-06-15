package com.falconteam.laboratorio_5.data.database.Entity

data class Comentarios(
    val id: String,
    val postId: String,
    val author: String,
    val comment: String
)