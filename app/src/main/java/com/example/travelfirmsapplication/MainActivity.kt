package com.example.travelfirmsapplication

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    val SBclient: SupabaseClient = CreateSupabaseClient().SBclient
    val roomsL: ArrayList<Room> = ArrayList()
    private val adapter = RoomsListAdapter(roomsL, object:RoomsListAdapter.ItemClickListener{
        override fun onItemClick(position: Int) {
            val bundle = Bundle()
            bundle.putString("page", "devicesInRoom")
            bundle.putInt("roomId", roomsL[position].id)
            intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Load_rooms_by_user()
        val recycler: RecyclerView = findViewById(R.id.recyclerRooms)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

    }

    private fun Load_rooms_by_user() {
        roomsL.clear()
        lifecycleScope.launch {
            try {
                val TextAddress: TextView = findViewById(R.id.textAddress)
                val user = SBclient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                val response = SBclient.postgrest["Users"].select(columns = Columns.list("address")){
                    eq("id", user.id)
                }.decodeSingle<User>()
                TextAddress.text = response.address
                val rooms = SBclient.postgrest["Rooms"].select() {
                    eq("user_id", user.id)
                }.body.toString()
                val builder = StringBuilder()
                builder.append(rooms).append("\n")
                val root = JSONArray(builder.toString())
                for (i in 0..<root.length()){
                    val obj = root.getJSONObject(i)
                    val id = obj.getInt("id")
                    val name = obj.getString("name")
                    val type = obj.getString("type")
                    roomsL.add(Room(id,name,type))
                }
                adapter.notifyDataSetChanged()
            } catch (e: Exception){
                Log.e("FAACCRRR", e.toString())
            }
        }
    }
}