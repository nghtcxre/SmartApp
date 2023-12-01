package com.example.travelfirmsapplication

import android.graphics.drawable.Drawable

data class Room(val id: String, val name: String, val type: String, val image: Drawable) {
    var isSelected: Boolean = false
}
