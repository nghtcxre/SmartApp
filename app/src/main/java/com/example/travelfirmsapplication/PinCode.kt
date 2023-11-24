package com.example.travelfirmsapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.coroutines.launch

class PinCode : AppCompatActivity() {

    private lateinit var pinDigits: StringBuilder
    private lateinit var circle1: ImageView
    private lateinit var circle2: ImageView
    private lateinit var circle3: ImageView
    private lateinit var circle4: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_code)

        sharedPreferences = getSharedPreferences("SHERED_PREF", Context.MODE_PRIVATE)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        pinDigits = StringBuilder()
        circle1 = findViewById(R.id.circle1)
        circle2 = findViewById(R.id.circle2)
        circle3 = findViewById(R.id.circle3)
        circle4 = findViewById(R.id.circle4)

        val buttonClickListener = View.OnClickListener { view ->
            if (view is Button) {
                if (pinDigits.length < 4) {
                    pinDigits.append(view.text)
                    updateCircleIndicators()
                }

                if (pinDigits.length == 4) {
                    val savedPinCode = sharedPreferences.getString("PIN_CODE", "")
                    if (pinDigits.toString() == savedPinCode) {
                        val hasAddress = sharedPreferences.getBoolean("HAS_ADDRESS", false)
                        if (!hasAddress){
                            val addressIntent = Intent(this@PinCode, AddAddress::class.java)
                            startActivity(addressIntent)
                            finish()
                        } else {
                            val mainIntent = Intent(this@PinCode, MainActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        }


                    } else {
                        Toast.makeText(this@PinCode, "Неверный пин-код", Toast.LENGTH_SHORT).show()
                        vibrate()
                        pinDigits.clear()
                        updateCircleIndicators()
                    }
                }

            }
        }

        val buttons = arrayOf(
            findViewById<Button>(R.id.btn1),
            findViewById<Button>(R.id.btn2),
            findViewById<Button>(R.id.btn3),
            findViewById<Button>(R.id.btn4),
            findViewById<Button>(R.id.btn5),
            findViewById<Button>(R.id.btn6),
            findViewById<Button>(R.id.btn7),
            findViewById<Button>(R.id.btn8),
            findViewById<Button>(R.id.btn9)
        )

        buttons.forEach { it.setOnClickListener(buttonClickListener) }
    }

    private fun vibrate() {
        resetCircleBackgrounds()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) run {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else run {
            @Suppress("DEPRECATION")
            vibrator.vibrate(200)
        }
    }

    private fun resetCircleBackgrounds() {
        circle1.setBackgroundResource(R.drawable.circle_background)
        circle2.setBackgroundResource(R.drawable.circle_background)
        circle3.setBackgroundResource(R.drawable.circle_background)
        circle4.setBackgroundResource(R.drawable.circle_background)
    }

    private fun updateCircleIndicators() {
        when (pinDigits.length) {
            1 -> circle1.setBackgroundResource(R.drawable.circle_filled_background)
            2 -> circle2.setBackgroundResource(R.drawable.circle_filled_background)
            3 -> circle3.setBackgroundResource(R.drawable.circle_filled_background)
            4 -> {
                circle4.setBackgroundResource(R.drawable.circle_filled_background)
            }
        }
    }

    fun ExitFromPinCode(view: View) { finishAffinity() }
}