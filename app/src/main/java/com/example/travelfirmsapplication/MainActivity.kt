package com.example.travelfirmsapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
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
import java.io.ByteArrayInputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private val SBclient: SupabaseClient = CreateSupabaseClient().SBclient
    private val roomsL: ArrayList<DataRoom> = ArrayList()
    private lateinit var array: JSONArray

    private val adapter = RoomsListAdapter(roomsL, object:RoomsListAdapter.ItemClickListener{
        override fun onItemClick(position: Int) {
            //val bundle = Bundle()
            //bundle.putString("page", "devicesInRoom")
            //bundle.putInt("roomId", roomsL[position].id)
        }
    })

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)

        val recycler: RecyclerView = findViewById(R.id.recyclerRooms)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        Load_rooms_by_user()

    }

    private fun showLoadingIndicator() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        progressBar.visibility = View.GONE
    }

    private fun Load_rooms_by_user() {
        showLoadingIndicator()
        roomsL.clear()
        lifecycleScope.launch {
            try {
                val TextAddress: TextView = findViewById(R.id.textAddress)
                val user = SBclient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                val response = SBclient.postgrest["Users"].select(columns = Columns.list("address")){
                    eq("id", user.id)
                }.decodeSingle<User>()
                TextAddress.text = response.address

                val rooms = SBclient.postgrest["Rooms"]
                    .select(
                        columns = Columns.raw(""" id, name, rooms_name Rooms_Type ( rooms_avatars ) """)
                    )
                    {
                        eq("user_id", user.id)
                    }

/*                val rooms = SBclient.postgrest["Rooms"].select() {
                    eq("user_id", user.id)
                }.body.toString()*/
                val builder = StringBuilder()
                builder.append(rooms).append("\n")
                val root = JSONArray(builder.toString())
                for (i in 0..<root.length()){
                    val obj = root.getJSONObject(i)
                    val id = obj.getString("id")
                    val name = obj.getString("name")
                    //val img = obj.getString("rooms_name")
                    val rooms_type_id = obj.getString("rooms_type_id")
                    val img = obj.getString("rooms_avatars")
                    //val img = rooms_type_id
                     lifecycleScope.launch {
                        try{
                            val bucket = SBclient.storage["imgs"]
                            val bytes = bucket.downloadPublic(img)
                            val is1: InputStream = ByteArrayInputStream(bytes)
                            val bmp: Bitmap = BitmapFactory.decodeStream(is1)
                            val dr = BitmapDrawable(resources, bmp)
                            val rooms = DataRoom(id, rooms_type_id, name, dr)
                            roomsL.add(rooms)
                        }
                        catch (e: Exception){
                            Log.e("!!!!!", e.toString())
                        }
                    }
                }
                hideLoadingIndicator()
                adapter.notifyDataSetChanged()
            } catch (e: Exception){
                Log.e("FAACCRRR", e.toString())
                hideLoadingIndicator()
            }
        }
    }



    fun toProfile(view: View) {
        intent = Intent(this@MainActivity, ProfileActivity::class.java)
        startActivity(intent)
    }
}