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
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class Registration : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var usernameEditText: EditText
    val SBclient: SupabaseClient = CreateSupabaseClient().SBclient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        sharedPreferences = getSharedPreferences("SHERED_PREF",Context.MODE_PRIVATE)
        usernameEditText = findViewById(R.id.editTextUsername)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword) }

        fun reg_Click(view: View) {

            lifecycleScope.launch {
                try {
                    SBclient.gotrue.signUpWith(io.github.jan.supabase.gotrue.providers.builtin.Email) {
                        email = emailEditText.text.toString()
                        password = passwordEditText.text.toString()
                     }
                    val isreg:Boolean=true
                    val editor:SharedPreferences.Editor=sharedPreferences.edit()
                    editor.putBoolean("ISREG",isreg)
                    editor.apply()
                    val user = SBclient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                    val userAdd = User(id = user.id,name = usernameEditText.text.toString())
                    SBclient.postgrest["Users"].insert(userAdd)
                    val regIntent = Intent(this@Registration, PinCodeCreate::class.java)
                    startActivity(regIntent)
                    finish()
                }
                catch (e: Exception) {
                    Log.e("!!!!", e.toString())
                }

            }
        }

    fun SignIn(view: View) {
        val signIn = Intent(this@Registration, LoginActivity::class.java)
        startActivity(signIn)
        finish()
    }
}