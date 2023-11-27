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
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    val SBclient: SupabaseClient = CreateSupabaseClient().SBclient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences("SHERED_PREF", Context.MODE_PRIVATE)
        //if(!sharedPreferences.getBoolean("ISREG",false)){
        //    startActivity(Intent(this, Registration::class.java))
       // }

        val textEmail: EditText = findViewById(R.id.editTextEmail)
        val textPassword: EditText = findViewById(R.id.editTextPassword)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            if(textEmail.text.toString() != "" && textPassword.text.toString() != ""){
                lifecycleScope.launch {
                    try {
                        SBclient.gotrue.loginWith(Email) {
                            email = textEmail.text.toString()
                            password = textPassword.text.toString()
                        }
                        val hasPinCode = sharedPreferences.getBoolean("HAS_PIN_CODE", false)
                        if (!hasPinCode) {
                            val pinCodeIntent = Intent(this@LoginActivity, PinCodeCreate::class.java)
                            startActivity(pinCodeIntent)
                            finish()
                        } else {
                            val pinCodeIntent = Intent(this@LoginActivity, PinCode::class.java)
                            startActivity(pinCodeIntent)
                            finish()
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