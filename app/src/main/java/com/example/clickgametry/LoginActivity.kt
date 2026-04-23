package com.example.clickgametry

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent
import androidx.core.widget.addTextChangedListener

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val playerOneName = findViewById<EditText>(R.id.playerOne_Name)
        val playerTwoName = findViewById<EditText>(R.id.playerTwo_Name)
        val login = findViewById<Button>(R.id.loginBtn)

        login.isEnabled = false

        fun checkFields() {
            val name1 = playerOneName.text.toString().trim()
            val name2 = playerTwoName.text.toString().trim()

            login.isEnabled = name1.isNotEmpty() && name2.isNotEmpty()
        }

        playerOneName.addTextChangedListener {
            checkFields()
        }

        playerTwoName.addTextChangedListener {
            checkFields()
        }


        login.setOnClickListener {
            val name1 = playerOneName.text.toString().trim()
            val name2 = playerTwoName.text.toString().trim()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("PLAYER_ONE", name1)
            intent.putExtra("PLAYER_TWO", name2)

            startActivity(intent)

        }
    }
}