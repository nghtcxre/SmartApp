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
import kotlinx.coroutines.launch

class Registration : AppCompatActivity() {

    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://rlssyraczenzujtfybve.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJsc3N5cmFjemVuenVqdGZ5YnZlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA1ODIwMTEsImV4cCI6MjAxNjE1ODAxMX0.dQd6AHO0giUIwJAZQjkXZ2ycT8mPdmcu8sbq1Nkpwvo"
    ) {
        install(GoTrue)
        install(Postgrest)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val usernameEditText: EditText = findViewById(R.id.editTextUsername)
        val emailEditText: EditText = findViewById(R.id.editTextEmail)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val registerButton: Button = findViewById(R.id.reg_button)

       // registerButton.setOnClickListener {
            //val userss = RegistrationRequest(username = "sdadsd", email = "rcursedov@mail.ru", password = "1231")
            //supabase.postgrest["users"].insert(userss, returning = Returning.MINIMAL)
            //val username = usernameEditText.text.toString()
       // }

    }

    private fun registerUser(email: String, password: String) {

    }

    private fun loginUser(email: String, password: String) {

    }

    fun reg_Click(view: View) {
        lifecycleScope.launch {
            try {
                val user = supabaseClient.gotrue.signUpWith(Email) {
                    email
                    password
                }
            }
            catch (e: Exception){
                Log.e("!!!!", e.toString())
            }
        }
    }
}