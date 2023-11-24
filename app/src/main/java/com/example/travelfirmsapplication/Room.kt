package com.example.travelfirmsapplication

import kotlinx.serialization.Serializable

@Serializable
data class Room(val id: Int, val name: String = "", val type: String = "")
