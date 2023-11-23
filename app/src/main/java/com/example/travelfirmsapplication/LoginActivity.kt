package com.example.travelfirmsapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

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
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(R.layout.activity_login)


        sharedPreferences = getSharedPreferences("SHERED_PREF", Context.MODE_PRIVATE)
        if(!sharedPreferences.getBoolean("ISREG",false)){
            startActivity(Intent(this, Registration::class.java))
        }

        val textEmail: EditText = findViewById(R.id.editTextEmail)
        val textPassword: EditText = findViewById(R.id.editTextPassword)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            if(textEmail.text.toString() != "" && textPassword.text.toString() != ""){
                lifecycleScope.launch {
                    try {
                        supabaseClient.gotrue.loginWith(Email) {
                            email = textEmail.text.toString()
                            password = textPassword.text.toString()
                        }
                        if (!sharedPreferences.getBoolean("pincode", false)){
                            val intent = Intent(this@LoginActivity, PinCode::class.java)
                            startActivity(intent)
                        }
                        else if (sharedPreferences.getBoolean("pincode", false)){
                            val intent = Intent(this@LoginActivity, PinCodeCreate::class.java)
                            startActivity(intent)
                        }
                    } catch (e: Exception) {
                        Log.e("Some error", e.toString())
                    }
                }
            }

        }
    }

    fun Reg(view: View)
    {
        val intent = Intent(this, Registration::class.java)
        startActivity(intent)
    }
}