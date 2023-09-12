package com.feeling.emotion.phpassion.planet

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.feeling.emotion.phpassion.R
import com.feeling.emotion.phpassion.databinding.ActivitySelectTerritoryBinding
import com.feeling.emotion.phpassion.game.GameEngineFactory
import com.feeling.emotion.phpassion.game.MainActivity
import com.feeling.emotion.phpassion.global.AbstractDAO
import com.feeling.emotion.phpassion.global.developer.DeveloperActivity

class SelectTerritoryActivity : AppCompatActivity() {

    companion object {
        const val MODE = "selectTerritoryMode"
        const val CONTINUE_WITH_PLAY_GAME = 0
        const val CONTINUE_WITH_RESET_GAME = 1
        const val CONTINUE_WITH_DEVELOPER_ACTIVITY = 2
    }

    lateinit var binding : ActivitySelectTerritoryBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectTerritoryBinding.inflate(layoutInflater);
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 21) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.navigationBackground)
        }
        AbstractDAO.init(this)

        val planet = GameEngineFactory().getPlanet()

        binding.territory1.setOnClickListener { selectTerritory(0, planet) }
        binding.territory2.setOnClickListener { selectTerritory(1, planet) }
        binding.territory3.setOnClickListener { selectTerritory(2, planet) }

        when (planet.gameDefinitions.size) {
            2 -> {
                set12(planet)
                binding.territory3.visibility = View.INVISIBLE
                binding.gameInfoView3.visibility = View.INVISIBLE
            }
            3 -> {
                set12(planet)
                binding.territory3.text = resources.getString(planet.gameDefinitions[2].territoryName)
                binding.gameInfoView3.text = getGameInfoText(2, planet)
            }
            else -> { // wrong value
                finish()
                return
            }
        }
    }

    private fun set12(planet: IPlanet) {
        binding.territory1.text = resources.getString(planet.gameDefinitions[0].territoryName)
        binding.territory2.text = resources.getString(planet.gameDefinitions[1].territoryName)
        binding.gameInfoView1.text = getGameInfoText(0, planet)
        binding.gameInfoView2.text = getGameInfoText(1, planet)
    }

    private fun getGameInfoText(gi: Int, planet: IPlanet): String {
        return planet.getGameInfo(resources, gi)
    }

    private fun selectTerritory(territoryNumber: Int, planet: IPlanet) {
        planet.selectedGame = planet.gameDefinitions[territoryNumber]
        when (intent.extras?.getInt(MODE)) {
            CONTINUE_WITH_RESET_GAME -> onNewLiberationAttemptQuestion()
            CONTINUE_WITH_DEVELOPER_ACTIVITY -> {
                finish()
                startActivity(Intent(this, DeveloperActivity::class.java))
            }
            else -> { // CONTINUE_WITH_PLAY_GAME
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    private fun onNewLiberationAttemptQuestion() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle(R.string.newLiberationAttemptQuestion)
        dialog.setPositiveButton(resources.getString(android.R.string.ok)) { _, _ -> onResetGame() }
        dialog.setNegativeButton(resources.getString(android.R.string.cancel), null)
        dialog.show()
    }

    private fun onResetGame() {
        GameEngineFactory().getPlanet().resetGame()
        finish()
    }
}