package com.example.travelfirmsapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.ByteArrayInputStream
import java.io.InputStream

class AddRoom : AppCompatActivity() {

    private val SBclient: SupabaseClient = CreateSupabaseClient().SBclient
    private val roomsList: ArrayList<Room> = ArrayList()
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: RoomsAddAdapter
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_room)
        recycler = findViewById(R.id.listRoomsRecycler)
        recycler.layoutManager = GridLayoutManager(applicationContext, 3)
        adapter = RoomsAddAdapter(roomsList, object:RoomsAddAdapter.ItemClickListener{
            override fun onItemClick(position: Int) {
                if (selectedPosition != RecyclerView.NO_POSITION) {
                    val previousSelectedRoom = roomsList[selectedPosition]
                    previousSelectedRoom.isSelected = false
                    adapter.notifyItemChanged(selectedPosition)
                }

                val selectedRoom = roomsList[position]
                selectedRoom.isSelected = true
                adapter.notifyItemChanged(position)

                selectedPosition = position
            }
        })
        recycler.adapter = adapter
        Load_rooms()
    }


    private fun Load_rooms() {
        roomsList.clear()
        lifecycleScope.launch{
            try {
                val user = SBclient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                val rooms = SBclient.postgrest["Rooms_Type"].select().body.toString()
                val builder = StringBuilder()
                builder.append(rooms).append("\n")
                val root = JSONArray(builder.toString())
                for (i in 0..<root.length()){
                    val obj = root.getJSONObject(i)
                    val id = obj.getString("id")
                    val name = obj.getString("name")
                    val roomsType = obj.getString("avatar")
                    val img = ("$roomsType.png")
                    val bucket = SBclient.storage["chooseRoom"]
                    val bytes = bucket.downloadPublic(img)
                    val is1: InputStream = ByteArrayInputStream(bytes)
                    val bmp: Bitmap = BitmapFactory.decodeStream(is1)
                    val dr = BitmapDrawable(resources, bmp)
                    roomsList.add(Room(id,name,roomsType,dr))
                }
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Произошла ошибка при выводе", Toast.LENGTH_SHORT).show()
                Log.e("Some error", e.toString())
            }
        }
    }

    fun BackToMain(view: View) {
        finish()
    }

}