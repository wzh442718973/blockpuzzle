package com.feeling.emotion.phpassion.global

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.feeling.emotion.phpassion.R
import com.feeling.emotion.phpassion.cluster.NavigationActivity
import com.feeling.emotion.phpassion.data.DataMarketActivity
import com.feeling.emotion.phpassion.databinding.ActivityBridgeBinding
import com.feeling.emotion.phpassion.game.GameEngineFactory
import com.feeling.emotion.phpassion.game.MainActivity
import com.feeling.emotion.phpassion.game.stonewars.deathstar.SpaceNebulaRoute
import com.feeling.emotion.phpassion.global.developer.DeveloperActivity
import com.feeling.emotion.phpassion.planet.IPlanet
import com.feeling.emotion.phpassion.planet.SelectTerritoryActivity

class BridgeActivity : AppCompatActivity() {

    lateinit var binding : ActivityBridgeBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBridgeBinding.inflate(layoutInflater);
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 21) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.navigationBackground)
        }
        AbstractDAO.init(this)

        binding.navigation.setOnClickListener { startActivity(Intent(this, NavigationActivity::class.java)) }
        binding.play.setOnClickListener { onPlay() }
        binding.newGame.setOnClickListener { onNewGame() }
        binding.dataexchange.setOnClickListener { startActivity(Intent(this, DataMarketActivity::class.java)) }
        binding.developer.visibility = if (Features.developerMode) View.VISIBLE else View.INVISIBLE
        binding.developer.setOnClickListener { onDeveloper() }
        binding.quitGame.setOnClickListener{ onQuitGame() }
    }

    override fun onResume() {
        super.onResume()
        try {
            update()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.javaClass.toString() + ": " + e.message + "\n" + e.stackTrace[0].toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() { // do nothing
    }

    private fun update() {
        val planet = getPlanet()
        binding.navigation.isEnabled = SpaceNebulaRoute.isNoDeathStarMode
        binding.positionView.text = planet.getInfo(resources) // all lines under Navigation button
        binding.play.isEnabled = isGameBtnEnabled(planet)
        binding.newGame.setText(planet.newLiberationAttemptButtonTextResId)
    }

    private fun onPlay() {
        if (getPlanet().userMustSelectTerritory()) {
            selectTerritory(SelectTerritoryActivity.CONTINUE_WITH_PLAY_GAME)
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun selectTerritory(mode: Int) {
        val intent = Intent(this, SelectTerritoryActivity::class.java)
        val args = Bundle()
        args.putInt(SelectTerritoryActivity.MODE, mode)
        intent.putExtras(args)
        startActivity(intent)
    }

    private fun onNewGame() {
        val planet = getPlanet()
        if (planet.userMustSelectTerritory()) {
            selectTerritory(SelectTerritoryActivity.CONTINUE_WITH_RESET_GAME)
        } else {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
            dialog.setTitle(planet.newLiberationAttemptQuestionResId)
            dialog.setPositiveButton(android.R.string.ok) { _, _ -> onResetGame() }
            dialog.setNegativeButton(android.R.string.cancel, null)
            dialog.show()
        }
    }

    private fun onResetGame() {
        GameEngineFactory().getPlanet().resetGame()
        update()
    }

    private fun isGameBtnEnabled(planet: IPlanet) = planet.hasGames()

    private fun onQuitGame() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle(R.string.leaveShipInSpace)
        dialog.setPositiveButton(resources.getString(android.R.string.ok)) { _, _ -> onQuitGame2() }
        dialog.setNegativeButton(resources.getString(android.R.string.cancel), null)
        dialog.show()
    }

    private fun onQuitGame2() {
        val gd = GlobalData.get()
        gd.gameType = GameType.NOT_SELECTED
        gd.save()
        finishAffinity() // App beenden
    }

    private fun onDeveloper() {
        if (getPlanet().userMustSelectTerritory()) {
            selectTerritory(SelectTerritoryActivity.CONTINUE_WITH_DEVELOPER_ACTIVITY)
        } else {
            startActivity(Intent(this, DeveloperActivity::class.java))
        }
    }

    private fun getPlanet() = GameEngineFactory().getPlanet()
}
