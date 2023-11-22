package com.example.travelfirmsapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Returning
import kotlinx.coroutines.launch
import kotlinx.serialization.json.put

class Registration : AppCompatActivity() {

    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://msrymwiawamltodzgdyr.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1zcnltd2lhd2FtbHRvZHpnZHlyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA2NDg2NDYsImV4cCI6MjAxNjIyNDY0Nn0.3PrDR_z3J8nWHIrzlBjhRuJmN235jgDydjUX8Xlj01s"
    ) {
        install(GoTrue)
        install(Postgrest)
    }
    val gotrue = supabaseClient.gotrue

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val usernameEditText: EditText = findViewById(R.id.editTextUsername)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)

        }


        fun reg_Click(view: View) {
            lifecycleScope.launch {
                try {
                    supabaseClient.gotrue.signUpWith(io.github.jan.supabase.gotrue.providers.builtin.Email) {
                        email = emailEditText.text.toString()
                        password = passwordEditText.text.toString()
                     }
                } catch (e: Exception) {
                    Log.e("!!!!", e.toString())
                }

            }
        }
    }