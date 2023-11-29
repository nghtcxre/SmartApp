package com.example.travelfirmsapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.SupabaseClient

class RoomsListAdapter(private val list_rooms: ArrayList<DataRoom>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RoomsListAdapter.RoomViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(position: Int)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int

    ): RoomsListAdapter.RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomsListAdapter.RoomViewHolder, position: Int) {
        val room: DataRoom = list_rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int {
        return list_rooms.size
    }

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomName: TextView = itemView.findViewById(R.id.roomName)
        private val roomPhoto: ImageView = itemView.findViewById(R.id.roomPhoto)

        fun bind(room: DataRoom) {
            roomName.text = room.name
            roomPhoto.setImageDrawable(room.image)

                itemView.setOnClickListener()
                    {
                        itemClickListener.onItemClick(adapterPosition)
                    }
        }
    }
}