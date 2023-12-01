package com.example.travelfirmsapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import java.lang.reflect.Array.set
import java.util.regex.Pattern

class AddAddress : AppCompatActivity() {

    private lateinit var textInputEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences

    val SBclient: SupabaseClient = CreateSupabaseClient().SBclient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        sharedPreferences = getSharedPreferences("SHERED_PREF", Context.MODE_PRIVATE)
        textInputEditText = findViewById(R.id.textInputEditText)
    }

    fun SaveAddress(view: View) {
        val addressPattern = Pattern.compile("г\\. [\\p{L} ]+, ул\\. [\\p{L} ]+, д\\. \\d+")
        val userInput = textInputEditText.text.toString()
        if (isValidAddress(userInput, addressPattern)){
            lifecycleScope.launch {
                try {
                    val user = SBclient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                    SBclient.postgrest["Users"].update(
                        {
                            set("address", textInputEditText.text.toString())
                        }
                    ) {
                        eq("id", user.id)
                    }
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putBoolean("HAS_ADDRESS", true)
                    editor.apply()
                    val mainIntent = Intent(this@AddAddress, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }

                catch (e: Exception) {
                    Log.e("AddAddress", e.toString())
                }
            }

        }
        else{
            Toast.makeText(applicationContext, "Введите адрес согласно формату", Toast.LENGTH_SHORT).show()
        }
    }

    fun isValidAddress(address: String, pattern: Pattern): Boolean {
        val matcher = pattern.matcher(address)
        return matcher.matches()
    }
}