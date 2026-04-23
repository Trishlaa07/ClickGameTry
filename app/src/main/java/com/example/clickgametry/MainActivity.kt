package com.example.clickgametry

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var playerOneCount: Int = 0
    var playerTwoCount: Int = 0
    var isGameOn: Boolean = false
    var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val playerOneScore = findViewById<TextView>(R.id.txt_playerOneScore)
        val playerTwoScore = findViewById<TextView>(R.id.txt_playerTwoScore)

        val playerOne = findViewById<Button>(R.id.btn_playerOne)
        val playerTwo = findViewById<Button>(R.id.btn_playerTwo)
        val startGame = findViewById<Button>(R.id.btn_start_game)
        val leaderboardBtn = findViewById<Button>(R.id.btn_leaderboard)
        val timerText = findViewById<TextView>(R.id.txt_timer)

        val playerOneName = intent.getStringExtra("PLAYER_ONE") ?: "Player 1"
        val playerTwoName = intent.getStringExtra("PLAYER_TWO") ?: "Player 2"

        playerOneScore.text = "$playerOneName Score: 0"
        playerTwoScore.text = "$playerTwoName Score: 0"

        startGame.setOnClickListener {

            if (isGameOn) {

                countDownTimer?.cancel()

                playerOne.isEnabled = false
                playerTwo.isEnabled = false
                startGame.text = "Start Game"
                isGameOn = false
                leaderboardBtn.isEnabled = true

                showResultDialog(playerOneName, playerTwoName)

            } else {

                playerOneCount = 0
                playerTwoCount = 0
                playerOneScore.text = "$playerOneName Score: 0"
                playerTwoScore.text = "$playerTwoName Score: 0"

                playerOne.isEnabled = true
                playerTwo.isEnabled = true

                startGame.text = "Stop Game"
                isGameOn = true
                leaderboardBtn.isEnabled = false

                timerText.text = "10"

                countDownTimer?.cancel()

                countDownTimer = object : CountDownTimer(10000, 1000) {

                    override fun onTick(millisUntilFinished: Long) {
                        val seconds = (millisUntilFinished / 1000) + 1
                        timerText.text = seconds.toString()
                    }

                    override fun onFinish() {
                        timerText.text = "Done!"
                        playerOne.isEnabled = false
                        playerTwo.isEnabled = false
                        startGame.text = "Start Game"
                        isGameOn = false
                        leaderboardBtn.isEnabled = true

                        showResultDialog(playerOneName, playerTwoName)
                    }

                }.start()
            }
        }

        playerOne.setOnClickListener {
            if (isGameOn) {
                playerOneCount++
                playerOneScore.text = "$playerOneName Score: $playerOneCount"
            }
        }

        playerTwo.setOnClickListener {
            if (isGameOn) {
                playerTwoCount++
                playerTwoScore.text = "$playerTwoName Score: $playerTwoCount"
            }
        }

        leaderboardBtn.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }

    fun showResultDialog(playerOneName: String, playerTwoName: String) {

        if (playerOneCount != 0 || playerTwoCount != 0) {

            val result = if (playerOneCount > playerTwoCount) {
                "$playerOneName ($playerOneCount) vs $playerTwoName ($playerTwoCount) → $playerOneName Won"
            } else if (playerTwoCount > playerOneCount) {
                "$playerOneName ($playerOneCount) vs $playerTwoName ($playerTwoCount) → $playerTwoName Won"
            } else {
                "$playerOneName ($playerOneCount) vs $playerTwoName ($playerTwoCount) → Draw"
            }

            val sharedPref = getSharedPreferences("LEADERBOARD", MODE_PRIVATE)
            val oldData = sharedPref.getString("scores", "") ?: ""
            val newData = if (oldData.isEmpty()) result else oldData + "|" + result
            sharedPref.edit().putString("scores", newData).apply()
        }

        val message = if (playerOneCount == 0 && playerTwoCount == 0) {
            "Game not started yet"
        } else if (playerOneCount > playerTwoCount) {
            "$playerOneName Wins!\nScore: $playerOneCount vs $playerTwoCount"
        } else if (playerTwoCount > playerOneCount) {
            "$playerTwoName Wins!\nScore: $playerTwoCount vs $playerOneCount"
        } else {
            "Draw!\nScore: $playerOneCount vs $playerTwoCount"
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game Over")
        builder.setMessage(message)
        builder.setCancelable(false)

        builder.setPositiveButton("Replay") { _, _ ->
            recreate()
        }

        builder.setNegativeButton("Back") { _, _ ->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        builder.show()
    }
}