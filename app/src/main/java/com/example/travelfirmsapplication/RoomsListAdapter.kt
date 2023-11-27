package com.example.travelfirmsapplication

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.InputStream

class RoomsListAdapter(private val list_rooms: ArrayList<Room>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<RoomsListAdapter.RoomViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(position: Int)

    }

    val SBclient: SupabaseClient = CreateSupabaseClient().SBclient

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int

    ): RoomsListAdapter.RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }


    override fun onBindViewHolder(holder: RoomsListAdapter.RoomViewHolder, position: Int) {
        val room: Room = list_rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int {
        return list_rooms.size
    }

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomName: TextView = itemView.findViewById(R.id.roomName)
        private val roomPhoto: ImageView = itemView.findViewById(R.id.roomPhoto)

        fun bind(room: Room) {
            roomName.text = room.name
                CoroutineScope(Dispatchers.Main).launch {
                try {
                    val bucket = SBclient.storage["imgs"]
                    val bytes_one = bucket.downloadPublic("bathroom.png")
                    val bytes_two = bucket.downloadPublic("beg.png")
                    val bytes_three = bucket.downloadPublic("kitchen.ng")
                    val bytes_four = bucket.downloadPublic("living.png")
                    val bytes_five = bucket.downloadPublic("office.png")
                    val bytes_six = bucket.downloadPublic("tv.png")
                    val is1: InputStream = ByteArrayInputStream(bytes_one)
                    val is2: InputStream = ByteArrayInputStream(bytes_two)
                    val is3: InputStream = ByteArrayInputStream(bytes_three)
                    val is4: InputStream = ByteArrayInputStream(bytes_four)
                    val is5: InputStream = ByteArrayInputStream(bytes_five)
                    val is6: InputStream = ByteArrayInputStream(bytes_six)
                    val bmp1: Bitmap = BitmapFactory.decodeStream(is1)
                    val bmp2: Bitmap = BitmapFactory.decodeStream(is2)
                    val bmp3: Bitmap = BitmapFactory.decodeStream(is3)
                    val bmp4: Bitmap = BitmapFactory.decodeStream(is4)
                    val bmp5: Bitmap = BitmapFactory.decodeStream(is5)
                    val bmp6: Bitmap = BitmapFactory.decodeStream(is6)
                    val dr1: Drawable = BitmapDrawable(Resources.getSystem(),bmp1)
                    val dr2: Drawable = BitmapDrawable(Resources.getSystem(),bmp2)
                    val dr3: Drawable = BitmapDrawable(Resources.getSystem(),bmp3)
                    val dr4: Drawable = BitmapDrawable(Resources.getSystem(),bmp4)
                    val dr5: Drawable = BitmapDrawable(Resources.getSystem(),bmp5)
                    val dr6: Drawable = BitmapDrawable(Resources.getSystem(),bmp6)
                    when(room.type.lowercase())
                    {
                        "bathroom" -> roomPhoto.setImageDrawable(dr1)
                        "beg" -> roomPhoto.setImageDrawable(dr2)
                        "kitchen" -> roomPhoto.setImageDrawable(dr3)
                        "living" -> roomPhoto.setImageDrawable(dr4)
                        "office" -> roomPhoto.setImageDrawable(dr5)
                        "tv" -> roomPhoto.setImageDrawable(dr6)
                    }
                } catch (e: Exception) {
                    Log.e("Error", e.toString())
                }
            }

        itemView.setOnClickListener()
            {
            itemClickListener.onItemClick(adapterPosition)
            }
        }
    }
}