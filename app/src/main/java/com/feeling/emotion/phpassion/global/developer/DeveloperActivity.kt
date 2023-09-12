package com.feeling.emotion.phpassion.global.developer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.feeling.emotion.phpassion.R
import com.feeling.emotion.phpassion.databinding.ActivityDeveloperBinding
import com.feeling.emotion.phpassion.game.GameEngineFactory
import com.feeling.emotion.phpassion.gamestate.Spielstand
import com.feeling.emotion.phpassion.gamestate.SpielstandDAO
import com.feeling.emotion.phpassion.gamestate.TrophyService
import com.feeling.emotion.phpassion.global.AbstractDAO
import com.feeling.emotion.phpassion.global.Features
import com.feeling.emotion.phpassion.planet.IPlanet
import com.feeling.emotion.phpassion.planet.SpaceObjectStateDAO
import com.feeling.emotion.phpassion.planet.SpaceObjectStateService

class DeveloperActivity : AppCompatActivity() {
    private val spielstandDAO = SpielstandDAO()
    private val sosDAO = SpaceObjectStateDAO()
    private var planet: IPlanet? = null
    private var index: Int = 0
    private var ss: Spielstand? = null

    lateinit var binding:ActivityDeveloperBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeveloperBinding.inflate(layoutInflater);
        setContentView(binding.root)

        if (!Features.developerMode) throw RuntimeException("Not allowed")
        AbstractDAO.init(this)

        binding.saveScore.setOnClickListener { onSave() }
        binding.liberated.setOnClickListener { onLiberated() }
        binding.conquered.setOnClickListener { onConquered() }
        binding.saveOtherScore.setOnClickListener { onSaveOther() }
        binding.saveNextRound.setOnClickListener { onSaveNextRound() }
        binding.saveTodayDate.setOnClickListener { onSaveTodayDate() }
        binding.deleteTrophies.setOnClickListener { onDeleteTrophies() }

        binding.resetAllBtn.setOnClickListener { onResetAll() }
        binding.openMap.setOnClickListener { onOpenMap() }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        load()

        binding.saveScore.isEnabled = (planet != null)
        binding.liberated.isEnabled = (planet != null)
        binding.conquered.isEnabled = (planet != null)
        binding.saveOtherScore.isEnabled = (planet != null)
        binding.saveNextRound.isEnabled = (planet != null)

        binding.score.setText("")
        binding.ownername.text = " "
        binding.otherScore.setText("")
        binding.otherMoves.setText("")
        if (ss != null) {
            binding.score.setText("" + ss!!.score)
            binding.ownername.text = ss!!.ownerName
            binding.otherScore.setText("" + ss!!.ownerScore)
            binding.otherMoves.setText("" + ss!!.ownerMoves)
            binding.nextRound.setText("" + ss!!.nextRound)
        }
        binding.todayDate.setText(DeveloperService().loadToday())
    }

    private fun load() {
        planet = GameEngineFactory().getPlanet()
        if (planet == null) {
            index = 0
            ss = null
        } else {
            index = planet!!.gameDefinitions.indexOf(planet!!.selectedGame)
            ss = SpielstandDAO().load(planet)
        }
    }

    private fun save() {
        spielstandDAO.save(planet, index, ss)
    }

    private fun onSave() {
        if (ss != null) {
            ss!!.score = Integer.parseInt(binding.score.text.toString())
            if (ss!!.score <= 0) {
                ss!!.moves = 0
            }
            save()
            finish()
        }
    }

    private fun onLiberated() {
        if (planet != null) {
            val sos = sosDAO.load(planet)
            sos.isOwner = true
            sosDAO.save(planet, sos)
            finish()
        }
    }

    private fun onConquered() {
        if (ss != null) {
            val sos = sosDAO.load(planet)
            sos.isOwner = false
            sosDAO.save(planet, sos)
            ss!!.unsetScore()
            ss!!.moves = 0
            save()
            finish()
        }
    }

    private fun onSaveOther() {
        if (ss != null) {
            ss!!.ownerScore = Integer.parseInt(binding.otherScore.text.toString())
            ss!!.ownerMoves = Integer.parseInt(binding.otherMoves.text.toString())
            ss!!.ownerName = "Detlef"
            save()
            finish()
        }
    }

    private fun onSaveNextRound() {
        if (ss != null) {
            ss!!.nextRound =  Integer.parseInt(binding.nextRound.text.toString())
            save()
            finish()
        }
    }

    private fun onSaveTodayDate() {
        DeveloperService().saveToday(binding.todayDate.text.toString())
        finish()
    }

    private fun onDeleteTrophies() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("Wirklich alle Trophäen auf 0 setzen?")
        dialog.setPositiveButton(resources.getString(android.R.string.ok)) { _, _ -> deleteAllTrophies() }
        dialog.setNegativeButton(resources.getString(android.R.string.cancel), null)
        dialog.show()
    }

    private fun deleteAllTrophies() {
        TrophyService().clear()
        finish()
    }

    private fun onOpenMap() {
        SpaceObjectStateService().openMap()
        finish()
    }

    private fun onResetAll() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("ACHTUNG: Wirklich ALLE Daten löschen?")
        dialog.setPositiveButton(resources.getString(android.R.string.ok)) { _, _ -> DeveloperService().resetAll() }
        dialog.setNegativeButton(resources.getString(android.R.string.cancel), null)
        dialog.show()
    }
}