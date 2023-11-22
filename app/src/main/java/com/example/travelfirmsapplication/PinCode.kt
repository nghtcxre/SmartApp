package com.example.travelfirmsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView

class PinCode : AppCompatActivity() {

    private lateinit var pinDigits: StringBuilder
    private lateinit var circle1: ImageView
    private lateinit var circle2: ImageView
    private lateinit var circle3: ImageView
    private lateinit var circle4: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pincode)
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
}