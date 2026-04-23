package com.example.clickgametry

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LeaderboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leaderboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val closeBtn = findViewById<Button>(R.id.btn_close)
        val container = findViewById<LinearLayout>(R.id.leaderboard_container)

        val sharedPref = getSharedPreferences("LEADERBOARD", MODE_PRIVATE)
        val data = sharedPref.getString("scores", "") ?: ""

        val list = data.split("|")

        for (item in list.reversed()) {
            if (item.isNotEmpty()) {

                val textView = TextView(this)
                textView.text = item
                textView.textSize = 16f
                textView.setPadding(24, 24, 24, 24)

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 16)
                textView.layoutParams = params

                textView.elevation = 6f

                container.addView(textView)
            }
        }
        closeBtn.setOnClickListener {
            finish()
        }

    }
}