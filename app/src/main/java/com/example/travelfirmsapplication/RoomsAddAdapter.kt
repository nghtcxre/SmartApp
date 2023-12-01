package com.example.travelfirmsapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.content.Context
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import io.ktor.client.engine.callContext
import kotlin.coroutines.coroutineContext

class RoomsAddAdapter(public val list_add: ArrayList<Room>, public val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RoomsAddAdapter.RoomViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsAddAdapter.RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_newroom, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomsAddAdapter.RoomViewHolder, position: Int) {
        val room: Room = list_add[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int {
        return list_add.size
    }

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomName: TextView = itemView.findViewById(R.id.nameRoomTextView)
        val roomPhoto: ImageView = itemView.findViewById(R.id.imageRoomImageView)

        init {
            itemView.setOnClickListener{
                itemClickListener.onItemClick(adapterPosition)
            }
        }
        fun bind(room: Room) {
            roomName.text = room.name
            roomPhoto.setImageDrawable(room.image)

            if (room.isSelected){
                roomName.setTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
                roomPhoto.setBackgroundResource(R.drawable.round_profile)
            } else {
                roomName.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey))
                roomPhoto.setBackgroundResource(R.drawable.round_room)
            }


        }
    }
}