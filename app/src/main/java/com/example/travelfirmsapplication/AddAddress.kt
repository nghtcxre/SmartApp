package com.example.travelfirmsapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class AddAddress : AppCompatActivity() {

    private lateinit var textInputEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences

    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://msrymwiawamltodzgdyr.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1zcnltd2lhd2FtbHRvZHpnZHlyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA2NDg2NDYsImV4cCI6MjAxNjIyNDY0Nn0.3PrDR_z3J8nWHIrzlBjhRuJmN235jgDydjUX8Xlj01s"
    ) {
        install(GoTrue)
        install(Postgrest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        sharedPreferences = getSharedPreferences("SHERED_PREF", Context.MODE_PRIVATE)
        textInputEditText = findViewById(R.id.textInputEditText)
    }

    fun SaveAddress(view: View) {
        lifecycleScope.launch {
            try {
                val user = supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                val toUpsert = User(id = user.id, address = textInputEditText.text.toString())
                supabaseClient.postgrest["Users"].insert(toUpsert, upsert = true)
            }

            catch (e: Exception) {
                Log.e("AddAddress", e.toString())
            }
        }
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("HAS_ADDRESS", true)
        editor.apply()
        val mainIntent = Intent(this@AddAddress, MainActivity::class.java)
        startActivity(mainIntent)
        finish()

    }
}