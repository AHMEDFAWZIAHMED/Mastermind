package a7m3d.game.mastermind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    private val hiddenNumbers = arrayListOf<Int>()
    private val userNumbers = arrayListOf<Int>()
    private val guessCircles = arrayListOf<AppCompatImageView>()
    private val resultCircles = arrayListOf<AppCompatImageView>()
    private lateinit var enterButton: AppCompatImageButton
    private lateinit var deleteButton: AppCompatImageButton
    private val circleImgs = listOf(
        R.drawable.black_circle, R.drawable.purple_circle, R.drawable.blue_circle,
        R.drawable.green_circle, R.drawable.yellow_circle, R.drawable.orange_circle,
        R.drawable.red_circle
    )
    private val circlesID = listOf(
        R.id.guess_11, R.id.guess_12, R.id.guess_13, R.id.guess_14,
        R.id.guess_21, R.id.guess_22, R.id.guess_23, R.id.guess_24,
        R.id.guess_31, R.id.guess_32, R.id.guess_33, R.id.guess_34,
        R.id.guess_41, R.id.guess_42, R.id.guess_43, R.id.guess_44,
        R.id.guess_51, R.id.guess_52, R.id.guess_53, R.id.guess_54,
        R.id.guess_61, R.id.guess_62, R.id.guess_63, R.id.guess_64,
        R.id.guess_71, R.id.guess_72, R.id.guess_73, R.id.guess_74
    )
    private val resultsID = listOf(
        R.id.result_11, R.id.result_12, R.id.result_13, R.id.result_14,
        R.id.result_21, R.id.result_22, R.id.result_23, R.id.result_24,
        R.id.result_31, R.id.result_32, R.id.result_33, R.id.result_34,
        R.id.result_41, R.id.result_42, R.id.result_43, R.id.result_44,
        R.id.result_51, R.id.result_52, R.id.result_53, R.id.result_54,
        R.id.result_61, R.id.result_62, R.id.result_63, R.id.result_64,
        R.id.result_71, R.id.result_72, R.id.result_73, R.id.result_74
    )
    private val buttonsID = listOf(
        R.id.black_button, R.id.purple_button, R.id.blue_button,
        R.id.green_button, R.id.yellow_button, R.id.orange_button,
        R.id.red_button
    )
    private var count = 0
    private var notValidEnter = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enterButton = findViewById(R.id.enter_button)
        enterButton.setOnClickListener { results() }
        deleteButton = findViewById(R.id.delete_button)
        deleteButton.setOnClickListener { deleteChoice() }

        for (i in buttonsID.indices) {
            val button = findViewById<AppCompatImageButton>(buttonsID[i])
            button.setOnClickListener { onColorClicked(i) }
        }
        for (id in circlesID) {
            val guessCircle = findViewById<AppCompatImageView>(id)
            guessCircles.add(guessCircle)
        }
        for (id in resultsID) {
            val resultCircle = findViewById<AppCompatImageView>(id)
            resultCircles.add(resultCircle)
        }

        cpuChoice()
    }

    private fun onColorClicked(indx: Int) {
        if (count >= guessCircles.size || notValidEnter) {
            Toast.makeText(
                this, "Maximum colors is 4 for one turn", Toast.LENGTH_SHORT
            ).show()
            return
        }
        guessCircles[count].setImageResource(circleImgs[indx])
        count++
        userNumbers.add(indx)
        if (count % 4 == 0) notValidEnter = true
    }

    private fun results() {
        if (count % 4 != 0 || userNumbers.isEmpty()) {
            Toast.makeText(
                this, "Must be 4 colors for one turn", Toast.LENGTH_SHORT
            ).show()
            return
        }
        val index = count - 4
        val unique = arrayListOf<Int>()
        var resultCount = 0
        for (i in 0..3) {
            if (userNumbers[i] == hiddenNumbers[i]) {
                resultCircles[index+resultCount].setImageResource(R.drawable.correct_position)
                unique.add(userNumbers[i])
                resultCount++
            }
        }
        if (userNumbers == hiddenNumbers) {
            gameOver("You got it!")
            return
        }
        for (i in 0..3) {
            if (userNumbers[i] in hiddenNumbers && userNumbers[i] !in unique) {
                resultCircles[index+resultCount].setImageResource(R.drawable.wrong_position)
                unique.add(userNumbers[i])
                resultCount++
            }
        }

        if (count == 28) gameOver("Best luck next time")
        notValidEnter = false
        userNumbers.clear()
    }

    private fun deleteChoice() {
        if (userNumbers.isEmpty()) {
            Toast.makeText(
                this, "Only delete colors in this turn!", Toast.LENGTH_SHORT
            ).show()
            return
        }
        count--
        guessCircles[count].setImageResource(R.drawable.white_circle)
        userNumbers.removeLast()
        notValidEnter = false
    }

    private fun cpuChoice() {
        val cpu = generateSequence { Random.nextInt(0..6)}.distinct().take(4).toList()
        for (c in cpu) hiddenNumbers += c
    }

    private fun gameOver(gameResult: String) {
        showCpuChoice()
        val dialog = AlertDialog.Builder(this)
        dialog.setCancelable(false)
        dialog.setTitle(gameResult)
        dialog.setNegativeButton("Play again") { d, _ ->
            d.dismiss()
            recreate()
        }
        dialog.setPositiveButton("Exit") { _, _ -> finish() }
        dialog.show()
    }

    private fun showCpuChoice() {
        val cpuChoice1 = findViewById<AppCompatImageView>(R.id.cpu_choice_1)
        val cpuChoice2 = findViewById<AppCompatImageView>(R.id.cpu_choice_2)
        val cpuChoice3 = findViewById<AppCompatImageView>(R.id.cpu_choice_3)
        val cpuChoice4 = findViewById<AppCompatImageView>(R.id.cpu_choice_4)

        cpuChoice1.setImageResource(circleImgs[hiddenNumbers[0]])
        cpuChoice2.setImageResource(circleImgs[hiddenNumbers[1]])
        cpuChoice3.setImageResource(circleImgs[hiddenNumbers[2]])
        cpuChoice4.setImageResource(circleImgs[hiddenNumbers[3]])
    }
}