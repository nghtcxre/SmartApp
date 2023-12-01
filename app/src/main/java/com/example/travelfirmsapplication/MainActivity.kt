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
import java.io.ByteArrayInputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private val SBclient: SupabaseClient = CreateSupabaseClient().SBclient
    private val roomsL: ArrayList<DataRoom> = ArrayList()

    private val adapter = RoomsListAdapter(roomsL, object:RoomsListAdapter.ItemClickListener{
        override fun onItemClick(position: Int) {

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
                val rooms = SBclient.postgrest["Rooms"].select() {
                    eq("user_id", user.id)
                }.body.toString()
                val builder = StringBuilder()
                builder.append(rooms).append("\n")
                val root = JSONArray(builder.toString())
                for (i in 0..<root.length()){
                    val obj = root.getJSONObject(i)
                    val id = obj.getString("id")
                    val name = obj.getString("name")
                    val roomsType = obj.getString("rooms_type_id")
                    val img = "$roomsType.png"
                    val bucket = SBclient.storage["imgs"]
                    val bytes = bucket.downloadPublic(img)
                    val is1: InputStream = ByteArrayInputStream(bytes)
                    val bmp: Bitmap = BitmapFactory.decodeStream(is1)
                    val dr = BitmapDrawable(resources, bmp)
                    roomsL.add(DataRoom(id,roomsType,name,dr))
                }
                hideLoadingIndicator()
                adapter.notifyDataSetChanged()
            } catch (e: Exception){
                Toast.makeText(applicationContext, "Ошибка при загрузке комнат", Toast.LENGTH_LONG).show()
                Log.e("Some Error", e.toString())
                hideLoadingIndicator()
            }
        }
    }

    fun toProfile(view: View) {
        intent = Intent(this@MainActivity, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun AddRoom(view: View) {
        intent = Intent(this@MainActivity, AddRoom::class.java)
        startActivity(intent)
    }
}