package com.example.travelfirmsapplication

import android.graphics.drawable.Drawable
import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String = "",val name: String = "", val address: String)
