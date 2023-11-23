package com.example.travelfirmsapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class Registration : AppCompatActivity() {
private lateinit var sharedPreferences: SharedPreferences
    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://msrymwiawamltodzgdyr.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1zcnltd2lhd2FtbHRvZHpnZHlyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA2NDg2NDYsImV4cCI6MjAxNjIyNDY0Nn0.3PrDR_z3J8nWHIrzlBjhRuJmN235jgDydjUX8Xlj01s"
    ) {
        install(GoTrue)
        install(Postgrest)
    }

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var usernameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        sharedPreferences = getSharedPreferences("SHERED_PREF",Context.MODE_PRIVATE)
        usernameEditText = findViewById(R.id.editTextUsername)
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
                    val isreg:Boolean=true
                    val editor:SharedPreferences.Editor=sharedPreferences.edit()
                    editor.putBoolean("ISREG",isreg)
                    editor.apply()
                    val user = supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                    val userAdd = User(id = user.id,name = usernameEditText.text.toString())
                    supabaseClient.postgrest["Users"].insert(userAdd)
                    val intent = Intent(this@Registration, PinCodeCreate::class.java)
                    startActivity(intent)
                }
                catch (e: Exception) {
                    Log.e("!!!!", e.toString())
                }

            }
        }

    fun SignIn(view: View) {
        val intent = Intent(this@Registration, LoginActivity::class.java)
        startActivity(intent)
    }
}